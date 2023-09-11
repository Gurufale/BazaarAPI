package com.cartService.services;

import com.cartService.dto.CartItemRequest;
import com.cartService.dto.CartRequest;
import com.cartService.dto.CartResponse;
import com.cartService.dto.ProductResponse;
import com.cartService.entities.Cart;
import com.cartService.entities.CartItem;
import com.cartService.exceptions.CartNotFoundException;
import com.cartService.repositories.CartItemRepository;
import com.cartService.repositories.CartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final WebClient.Builder webClientBuilder;
    private final CartRepository cartRepository;
    private final ObjectMapper mapper;
    private final CartItemRepository cartItemRepository;

    public void create(CartRequest cartRequest) {
        Cart cart = Cart.builder().userId(cartRequest.userId()).cartItems(cartRequest.cartItemList()).build();
        cartRepository.save(cart);
    }

    @Transactional
    public CartResponse addItemsToCart(Long userId, List<CartItemRequest> cartItemRequests) {

        List<Long> productIds = cartItemRequests.stream()
                .map(CartItemRequest::productId)
                .collect(Collectors.toList());

        List<ProductResponse> productList = getProductResponses(productIds);

        Cart cartEntity = findOrCreateCart(userId);
        updateCartItems(cartEntity, productList);

        return createCartResponse(cartEntity);
    }

    private List<ProductResponse> getProductResponses(List<Long> productIds) {
        String urlToGetProducts = "http://localhost:8080/product/getProducts/" + productIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        return webClientBuilder.build().get().uri(urlToGetProducts).retrieve().bodyToFlux(ProductResponse.class).collectList().block();
    }

    private Cart findOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseThrow(() -> new CartNotFoundException("Cart not found for user with ID: " + userId));
    }

    private void updateCartItems(Cart cartEntity, List<ProductResponse> productList) {

        List<CartItem> cartItems = productList.stream()
                .map(productResponse -> CartItem.builder()
                        .cart(cartEntity)
                        .productId(productResponse.id())
                        .quantity(productResponse.quantity())
                        .build()).collect(Collectors.toList());

        cartEntity.setCartItems(cartItems);
        cartRepository.save(cartEntity);
    }

    private CartResponse createCartResponse(Cart cartEntity) {
        List<CartItem> cartItems = cartEntity.getCartItems();
        return CartResponse.builder()
                .id(cartEntity.getId())
                .userId(cartEntity.getUserId())
                .cartItems(cartItems).build();
    }

    public List<CartItemResponse> geCartItems(Long userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        Cart cartEntity = cart.orElseThrow(() -> new CartNotFoundException("Cart not found for user with ID: " + userId));
        List<CartItem> cartItems = cartItemRepository.findAllByCart(cartEntity);
        return cartItems.stream()
                .map(cartItem -> {
                    try {
                        return CartItemResponse.builder()
                                .id(cartItem.getId())
                                .cart(cartItem.getCart())
                                .productId(cartItem.getProductId())
                                .quantity(cartItem.getQuantity()).build();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }
}

