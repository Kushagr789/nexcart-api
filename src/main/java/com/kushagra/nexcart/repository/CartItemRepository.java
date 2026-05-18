package com.kushagra.nexcart.repository;

import com.kushagra.nexcart.entity.Cart;
import com.kushagra.nexcart.entity.CartItem;
import com.kushagra.nexcart.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);

    Optional<CartItem>
    findByCartAndProduct(
            Cart cart,
            Product product
    );
}