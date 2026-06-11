package com.kushagra.nexcart.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {

    private Long orderItemId;

    private Long productId;

    private String productName;

    private BigDecimal productPrice;

    private Integer quantity;

    private BigDecimal subtotal;
}