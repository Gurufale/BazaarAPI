package com.user.services;

import com.user.dto.AuthenticationRequest;
import com.user.dto.AuthenticationResponse;
import com.user.dto.UserRequest;
import com.user.entities.User;
import com.user.entities.Wallet;
import com.user.events.UserCreatedEvent;
import com.user.exception.UserAlreadyExistException;
import com.user.repository.UserRepository;
import com.user.utilities.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    public AuthenticationResponse register(UserRequest userRequest) {

        User user = getUser(userRequest);

        if (userExist(user)) {
            throw new UserAlreadyExistException("User Already exist");
        }

        assignWallet(user);
        userRepository.save(user);

        sendNotification(user);

        String token = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder().token(token).build();
    }

    private void assignWallet(User user) {
        Wallet wallet = Wallet.builder().balance(0).build();
        user.setWallet(wallet);
    }

    private User getUser(UserRequest userRequest) {
        return User.builder().email(userRequest.email()).phoneNumber(userRequest.phoneNumber())
                .preference(userRequest.preference())
                .password(passwordEncoder.encode(userRequest.password()))
                .role(userRequest.role()).wallet(userRequest.wallet())
                        .referredBy(userRequest.referredBy()).
                build();
    }

    private void sendNotification(User user) {
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent(user.getId(),user.getPreference(), user.getEmail(), user.getPhoneNumber(),user.getReferredBy());
        kafkaTemplate.send("UserCreatedTopic", userCreatedEvent);
        log.info("Registration Successful : Thank You " + userCreatedEvent);
    }

    private boolean userExist(User user) {
        User byMail = userRepository.findByEmail(user.getEmail()).orElse(null);
        User byNumber = userRepository.findByPhoneNumber(user.getPhoneNumber()).orElse(null);
        return byMail != null || byNumber != null;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder().token(token).build();
    }

    public String validateToken(String token, UserRequest userRequest) {
        User user = userRepository.findByEmail(userRequest.email()).orElseThrow();
        boolean tokenValid = jwtUtil.isTokenValid(token, user);
        if (tokenValid) return "Token Validated";
        else return "Invalid Token";
    }
}
