package com.kushagra.nexcart.dto.request;

import com.kushagra.nexcart.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateOrderStatusRequest {

    @NotNull
    private OrderStatus orderStatus;
}