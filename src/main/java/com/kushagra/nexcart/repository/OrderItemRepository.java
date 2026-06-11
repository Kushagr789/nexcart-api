package com.kushagra.nexcart.repository;

import com.kushagra.nexcart.entity.OrderItem;

import com.kushagra.nexcart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByProduct_Store_Owner(
            User owner
    );
}