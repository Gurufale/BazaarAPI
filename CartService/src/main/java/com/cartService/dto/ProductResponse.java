package com.cartService.dto;

public record ProductResponse(
        long id,
        String name,
        String description,
        double price,
        String category,
        int quantity
) {
}
