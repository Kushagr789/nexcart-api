package com.kushagra.nexcart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kushagra.nexcart.entity.Order;
import com.kushagra.nexcart.entity.User;
import com.kushagra.nexcart.enums.OrderStatus;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    // CUSTOMER

    List<Order> findByCustomerOrderByCreatedAtDesc(
            User customer
    );

    Optional<Order> findByIdAndCustomer(
            Long orderId,
            User customer
    );

    // ADMIN

    List<Order> findAllByOrderByCreatedAtDesc();

    List<Order> findByOrderStatusOrderByCreatedAtDesc(
            OrderStatus orderStatus
    );
}