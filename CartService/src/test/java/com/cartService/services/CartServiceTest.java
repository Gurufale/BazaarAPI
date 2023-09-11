package com.cartService.services;

import com.cartService.builder.TestCartBuilder;
import com.cartService.dto.CartItemRequest;
import com.cartService.dto.CartRequest;
import com.cartService.entities.Cart;
import com.cartService.entities.CartItem;
import com.cartService.repositories.CartItemRepository;
import com.cartService.repositories.CartRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    private TestCartBuilder testCartBuilder;
    private CartService cartService;
    @Mock
    private CartRepository cartRepository;

    @Mock
    ObjectMapper objectMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @MockBean
    private WebClient.Builder weclientBuilder;

    @BeforeEach
    void setup() {
        testCartBuilder = new TestCartBuilder();
        cartService = new CartService(weclientBuilder,cartRepository, objectMapper, cartItemRepository);
    }

    @Test
    void shouldCrateCartForUser()
    {
        List<CartItem> cartItemList = new ArrayList<>();
        CartRequest cartRequest = new CartRequest(1L, cartItemList);
        Cart cart = testCartBuilder.withUserId(cartRequest.userId()).withCartItemList(cartRequest.cartItemList()).build();

        ArgumentCaptor<Cart> cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);
        cartService.create(cartRequest);
        verify(cartRepository).save(cartArgumentCaptor.capture());
        Cart actualCart = cartArgumentCaptor.getValue();

        assertEquals(cartRequest.userId(),actualCart.getUserId());
    }

    @Test
    @Disabled
    void shouldAddItemsToCartWhenItemsSelected() throws JsonProcessingException {
        CartRequest cartRequest = new CartRequest(1L, new ArrayList<>());
        Cart cart = Cart.builder().userId(cartRequest.userId()).cartItems(cartRequest.cartItemList()).build();
        CartItemRequest cartItemRequest1 = new CartItemRequest(cart, 1L, 2);
        CartItemRequest cartItemRequest2 = new CartItemRequest(cart, 2L, 2);
        List<CartItemRequest> cartItemRequests = List.of(cartItemRequest1, cartItemRequest2);

        //ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);

        cartService.addItemsToCart(1L,cartItemRequests);
        verify(cartRepository).save(cart);
        // List<CartItemRequest> actualCartItems = listArgumentCaptor.getValue();
        // assertEquals(cartItemRequests.size(),actualCartItems.size());
    }

}
