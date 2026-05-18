package com.kushagra.nexcart.repository;

import com.kushagra.nexcart.entity.Order;
import com.kushagra.nexcart.entity.User;
import com.kushagra.nexcart.enums.OrderStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    Page<Order> findByCustomer(
            User customer,
            Pageable pageable
    );

    Page<Order> findByStatus(
            OrderStatus status,
            Pageable pageable
    );
}