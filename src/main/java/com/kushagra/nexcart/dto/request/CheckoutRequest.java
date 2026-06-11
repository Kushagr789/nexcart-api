package com.kushagra.nexcart.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CheckoutRequest {

    @NotNull(message = "Shipping address id is required")
    private Long shippingAddressId;
}