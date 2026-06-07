package com.kushagra.nexcart.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartResponse {

    private Long cartId;

    private Long customerId;

    private Integer totalItems;

    private BigDecimal totalAmount;

    private List<CartItemResponse> items;
}