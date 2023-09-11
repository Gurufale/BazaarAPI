package com.cartService.repositories;

import com.cartService.entities.Cart;
import com.cartService.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findAllByCart(Cart id);
}
