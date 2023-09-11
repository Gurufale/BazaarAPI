package com.cartService.dto;

import com.cartService.entities.Cart;

public record CartItemRequest(Cart cart,Long productId,int quantity) {
}
