package com.kushagra.nexcart.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.kushagra.nexcart.dto.response.OrderItemResponse;
import com.kushagra.nexcart.dto.response.OrderResponse;
import com.kushagra.nexcart.dto.response.OrderSummaryResponse;
import com.kushagra.nexcart.entity.Order;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;
    private final AddressMapper addressMapper;

    public OrderResponse toResponse(Order order) {

        List<OrderItemResponse> orderItems =
                order.getOrderItems()
                        .stream()
                        .map(orderItemMapper::toResponse)
                        .toList();

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .shippingAddress(
                        addressMapper.toResponse(
                                order.getShippingAddress()
                        )
                )
                .orderItems(orderItems)
                .build();
    }

    public OrderSummaryResponse toSummaryResponse(
            Order order
    ) {

        int totalItems = order.getOrderItems()
                .stream()
                .mapToInt(item -> item.getQuantity())
                .sum();

        return OrderSummaryResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .totalItems(totalItems)
                .build();
    }
}