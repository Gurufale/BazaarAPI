package com.cartService.builder;

import com.cartService.entities.Cart;
import com.cartService.entities.CartItem;

import java.util.List;

public class TestCartBuilder {

    private Long id;
    private Long userId;
    private List<CartItem> cartItemList;

    public TestCartBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TestCartBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public TestCartBuilder withCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
        return this;
    }

    public Cart build() {
        return Cart.builder().id(id).userId(userId).cartItems(cartItemList).build();
    }
}
