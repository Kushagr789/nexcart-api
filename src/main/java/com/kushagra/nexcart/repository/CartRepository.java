package com.kushagra.nexcart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kushagra.nexcart.entity.Cart;
import com.kushagra.nexcart.entity.User;
import com.kushagra.nexcart.enums.CartStatus;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCustomerAndStatus(
            User customer,
            CartStatus status
    );
}