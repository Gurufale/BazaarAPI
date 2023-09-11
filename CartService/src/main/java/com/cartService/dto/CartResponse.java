package com.cartService.dto;

import com.cartService.entities.CartItem;
import lombok.Builder;
import java.util.List;

@Builder
public record CartResponse(long id,long userId, List<CartItem> cartItems) {
}
