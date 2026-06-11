package com.kushagra.nexcart.mapper;

import org.springframework.stereotype.Component;

import com.kushagra.nexcart.dto.response.OrderItemResponse;
import com.kushagra.nexcart.entity.OrderItem;

@Component
public class OrderItemMapper {

    public OrderItemResponse toResponse(
            OrderItem orderItem
    ) {

        return OrderItemResponse.builder()
                .orderItemId(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .productName(
                        orderItem.getProductNameSnapshot()
                )
                .productPrice(
                        orderItem.getProductPriceSnapshot()
                )
                .quantity(orderItem.getQuantity())
                .subtotal(orderItem.getSubtotal())
                .build();
    }
}