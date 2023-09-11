package com.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.user.dto.AuthenticationRequest;
import com.user.dto.AuthenticationResponse;
import com.user.dto.UserRequest;
import com.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRequest userRequest) throws JsonProcessingException {
        return ResponseEntity.ok(userService.register(userRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token,@RequestBody UserRequest userRequest) {
        String response = userService.validateToken(token, userRequest);
        return ResponseEntity.ok(response);
    }
}
