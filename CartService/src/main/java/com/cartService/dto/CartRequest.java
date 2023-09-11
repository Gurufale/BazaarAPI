package com.cartService.dto;

import com.cartService.entities.CartItem;

import java.util.List;

public record CartRequest(long userId, List<CartItem> cartItemList) {
}
