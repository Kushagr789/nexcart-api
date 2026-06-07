package com.kushagra.nexcart.mapper;

import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.kushagra.nexcart.dto.response.CartItemResponse;
import com.kushagra.nexcart.entity.CartItem;

@Component
public class CartItemMapper {

    public CartItemResponse toResponse(CartItem cartItem) {

        return CartItemResponse.builder()
                .cartItemId(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .quantity(cartItem.getQuantity())
                .price(
                        cartItem.getPrice()
                                .setScale(2, RoundingMode.HALF_UP)
                )
                .subtotal(
                        cartItem.getSubtotal()
                                .setScale(2, RoundingMode.HALF_UP)
                )
                .build();
    }
}