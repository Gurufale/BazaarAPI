package com.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.dto.AuthenticationRequest;
import com.user.dto.UserRequest;
import com.user.exception.UserAlreadyExistException;
import com.user.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UserRequest> argumentCaptor;

    @Test
    @WithMockUser
    public void shouldRegisterUserWithDetails() throws Exception {
        UserRequest userRequest = new UserRequest("abc@example.com", "9158986369", "Email", "password", null, null,null);
        String json = objectMapper.writeValueAsString(userRequest);
        mockMvc.perform(post("/user/register").with(csrf()).content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        verify(userService).register(argumentCaptor.capture());
        UserRequest actualRequest = argumentCaptor.getValue();
        assertEquals(userRequest.email(),actualRequest.email() );
    }

    @Test
    @WithMockUser
    public void shouldRegisterUserWithReferredByAnotherUser() throws Exception {
        UserRequest userRequest = new UserRequest("abc@example.com", "9158986369", "Email", "password", null, "Referral_1",null);
        String json = objectMapper.writeValueAsString(userRequest);
        mockMvc.perform(post("/user/register").with(csrf()).content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        verify(userService).register(argumentCaptor.capture());
        UserRequest actualRequest = argumentCaptor.getValue();
        assertEquals(actualRequest.email(), userRequest.email());
        assertEquals(userRequest.referredBy(),actualRequest.referredBy());
    }

    @Test
    @WithMockUser
    void shouldThrowExceptionWhenUserAlreadyExist() throws Exception {
        UserRequest userRequest = new UserRequest("abc@example.com", "9158986369", "Email", "password", null, null,null);
        String json = objectMapper.writeValueAsString(userRequest);
        doThrow(new UserAlreadyExistException("User already exists")).when(userService).register(userRequest);

        mockMvc.perform(post("/user/register").with(csrf()).content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

        verify(userService).register(userRequest);
    }

    @Test
    @WithMockUser
    void shouldAuthenticateUserForGivenDetails() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("a@example.com", "1234");
        String json = objectMapper.writeValueAsString(authenticationRequest);
        mockMvc.perform(post("/user/authenticate").with(csrf()).content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        ArgumentCaptor<AuthenticationRequest> authenticationRequestArgumentCaptor = ArgumentCaptor.forClass(AuthenticationRequest.class);

        verify(userService).authenticate(authenticationRequestArgumentCaptor.capture());
        AuthenticationRequest actualAuthRequest = authenticationRequestArgumentCaptor.getValue();
        assertEquals(authenticationRequest.getEmail(),actualAuthRequest.getEmail());
    }

    @Test
    @WithMockUser
    void shouldValidateTokenForUser() throws Exception {
        UserRequest userRequest = new UserRequest("abc@example.com", "9158986369", "Email", "password", null, null,null);

        String json = objectMapper.writeValueAsString(userRequest);
        String token = "secret";

        mockMvc.perform(get("/user/validate").with(csrf()).param("token", token).content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        ArgumentCaptor<String> argumentCaptorString = ArgumentCaptor.forClass(String.class);

        verify(userService).validateToken(argumentCaptorString.capture(), argumentCaptor.capture());
        String tokenValue = argumentCaptorString.getValue();
        UserRequest actualUser = argumentCaptor.getValue();
        Assertions.assertEquals(tokenValue, token);
        Assertions.assertEquals( userRequest.email(),actualUser.email());
    }
}
