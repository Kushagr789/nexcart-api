package com.kushagra.nexcart.repository;

import com.kushagra.nexcart.entity.OrderItem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {
}