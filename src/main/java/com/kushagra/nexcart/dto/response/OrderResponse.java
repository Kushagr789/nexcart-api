package com.kushagra.nexcart.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.kushagra.nexcart.enums.OrderStatus;
import com.kushagra.nexcart.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {

    private Long orderId;

    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    private BigDecimal totalAmount;

    private Instant createdAt;

    private AddressResponse shippingAddress;

    private List<OrderItemResponse> orderItems;
}