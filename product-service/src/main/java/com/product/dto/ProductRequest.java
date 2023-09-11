package com.product.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest(
        Long id,
        @NotBlank String name,
        String description,
        double price,
        String category,
        int quantity
) {
}
