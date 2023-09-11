package com.user.dto;

import com.user.entities.Wallet;

public record UserRequest(
        String email,
        String phoneNumber,
        String preference,
        String password,
        String role,
        String referredBy,
        Wallet wallet
) {
}
