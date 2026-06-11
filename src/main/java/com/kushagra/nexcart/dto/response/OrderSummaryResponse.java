package com.kushagra.nexcart.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.kushagra.nexcart.enums.OrderStatus;
import com.kushagra.nexcart.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderSummaryResponse {

    private Long orderId;

    private BigDecimal totalAmount;

    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    private Instant createdAt;

    private Integer totalItems;
}