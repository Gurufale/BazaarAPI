package com.cartService.controller;

import com.cartService.dto.CartItemRequest;
import com.cartService.dto.CartResponse;
import com.cartService.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/items")
    public ResponseEntity addItemToCart(@PathVariable Long userId, @RequestBody List<CartItemRequest> cartItemRequestList) throws IOException {
        CartResponse cartResponse = cartService.addItemsToCart(userId, cartItemRequestList);
        return new ResponseEntity(cartResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity getCartItems(@PathVariable Long userId)
    {
        return ResponseEntity.ok(cartService.geCartItems(userId));
    }
}
