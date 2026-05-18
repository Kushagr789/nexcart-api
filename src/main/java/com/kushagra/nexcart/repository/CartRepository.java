package com.kushagra.nexcart.repository;

import com.kushagra.nexcart.entity.Cart;
import com.kushagra.nexcart.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository
        extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}