package com.cartService.controller;

import com.cartService.dto.CartItemRequest;
import com.cartService.dto.CartRequest;
import com.cartService.dto.CartResponse;
import com.cartService.entities.Cart;
import com.cartService.entities.CartItem;
import com.cartService.services.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @MockBean
    CartService cartService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldAddItemToCartWhenItemsSelected() throws Exception {
        CartRequest cartRequest = new CartRequest(1L, new ArrayList<>());
        Cart cart = Cart.builder().userId(cartRequest.userId()).cartItems(cartRequest.cartItemList()).build();
        CartItemRequest cartItemRequest1 = new CartItemRequest(cart, 1L, 2);
        CartItemRequest cartItemRequest2 = new CartItemRequest(cart, 2L, 2);
        List<CartItemRequest> cartItemRequests = List.of(cartItemRequest1, cartItemRequest2);

        List<CartItem> cartItems = List.of(CartItem.builder()
                        .productId(cartItemRequest1.productId())
                        .cart(cartItemRequest1.cart())
                        .quantity(cartItemRequest1.quantity()).build(),
                CartItem.builder().cart(cartItemRequest2.cart())
                        .productId(cartItemRequest2.productId())
                        .quantity(cartItemRequest2.quantity()).build());

        CartResponse cartResponse = CartResponse.builder().userId(1L).cartItems(cartItems).build();
        when(cartService.addItemsToCart(1L, cartItemRequests)).thenReturn(cartResponse);

        mockMvc.perform(post("/cart/{userId}/items", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemRequests)))
                .andExpect(status().isCreated());

        Assertions.assertEquals(cartResponse.userId(),cartRequest.userId());
    }
}
