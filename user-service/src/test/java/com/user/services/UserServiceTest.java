package com.user.services;

import com.user.builder.TestUserBuilder;
import com.user.dto.UserRequest;
import com.user.entities.User;
import com.user.events.UserCreatedEvent;
import com.user.exception.UserAlreadyExistException;
import com.user.repository.UserRepository;
import com.user.utilities.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtUtil jwtUtil;

    @Mock
    KafkaTemplate kafkaTemplate;

    @Mock
    AuthenticationManager authenticationManager;
    UserService userService;
    private TestUserBuilder testUserBuilder;

    @BeforeEach
    void setup() {
        testUserBuilder = new TestUserBuilder();
        userService = new UserService(userRepository, passwordEncoder, jwtUtil, authenticationManager, kafkaTemplate);
    }

    @Test
    void shouldCreateUserForGivenDetails() {
        UserRequest userRequest = new UserRequest("a@example.com", "1234567892", "email", "1234", null, null, null);
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.findByEmail(userRequest.email())).thenReturn(Optional.empty());
        userService.register(userRequest);
        verify(userRepository).save(argumentCaptor.capture());
        User actualRequest = argumentCaptor.getValue();
        assertEquals(userRequest.email(), actualRequest.getEmail());
    }

    @Test
    void shouldThrowAnExceptionIfUserAlreadyExist() {
        UserRequest userRequest = new UserRequest("abc@example.com", "1234567892", "Email", "password", null, null, null);
        User user = testUserBuilder.withEmail(userRequest.email()).withNumber(userRequest.phoneNumber()).withPassword(userRequest.password()).withPreference(userRequest.preference()).withRole(userRequest.role()).withWallet(userRequest.wallet()).build();

        when(userRepository.findByEmail(userRequest.email())).thenReturn(Optional.ofNullable(user));
        UserAlreadyExistException userAlreadyExistException = assertThrows(UserAlreadyExistException.class, () -> userService.register(userRequest));
        assertEquals("User Already exist", userAlreadyExistException.getMessage());
    }

    @Test
    void shouldPublishUserCreatedEventAfterSuccessfulRegistration() {
        UserRequest userRequest = new UserRequest("abc@example.com", "1234567892", "Email", "password", null, null, null);
        ArgumentCaptor<UserCreatedEvent> argumentCaptor = ArgumentCaptor.forClass(UserCreatedEvent.class);
        ArgumentCaptor<String> topicArgumentCaptor = ArgumentCaptor.forClass(String.class);
        userService.register(userRequest);
        verify(kafkaTemplate).send(topicArgumentCaptor.capture(), argumentCaptor.capture());
        String email = argumentCaptor.getValue().getEmail();
        assertEquals(email, userRequest.email());
    }

    @Test
    void shouldAssignWalletToUserWhenRegistrationSuccessful() {
        UserRequest userRequest = new UserRequest("a@example.com", "1234567892", "email", "1234", null, null, null);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.findByEmail(userRequest.email())).thenReturn(Optional.empty());
        userService.register(userRequest);
        verify(userRepository).save(argumentCaptor.capture());
        User actualRequest = argumentCaptor.getValue();
        assertEquals(userRequest.email(), actualRequest.getEmail());
        assertNotNull(actualRequest.getWallet());
    }

    @Test
    void shouldValidateGivenTokenDetails() {
        UserRequest userRequest = new UserRequest("a@example.com", "1234567892", "email", "1234", null, null, null);
        User user = testUserBuilder.withEmail(userRequest.email()).withNumber(userRequest.phoneNumber()).withPassword(userRequest.password()).withPreference(userRequest.preference()).withRole(userRequest.role()).withWallet(userRequest.wallet()).build();
        String token = "token";
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<String> tokenArgumentCapture = ArgumentCaptor.forClass(String.class);

        when(userRepository.findByEmail(userRequest.email())).thenReturn(Optional.ofNullable(user));
        when(jwtUtil.isTokenValid(tokenArgumentCapture.capture(), argumentCaptor.capture())).thenReturn(true);

        String tokenValidated = userService.validateToken(token, userRequest);

        Assertions.assertEquals(tokenValidated, "Token Validated");
    }
}

