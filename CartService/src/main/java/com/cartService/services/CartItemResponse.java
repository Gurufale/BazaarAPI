package com.cartService.services;

import com.cartService.entities.Cart;
import lombok.Builder;

@Builder
public record CartItemResponse(Long id,Cart cart, Long productId, int quantity) {
}
