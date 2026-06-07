package com.kushagra.nexcart.mapper;

import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Component;

import com.kushagra.nexcart.dto.response.CartItemResponse;
import com.kushagra.nexcart.dto.response.CartResponse;
import com.kushagra.nexcart.entity.Cart;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CartMapper {

    private final CartItemMapper cartItemMapper;

    public CartResponse toResponse(Cart cart) {

        List<CartItemResponse> items = cart.getCartItems()
                .stream()
                .map(cartItemMapper::toResponse)
                .toList();

        int totalItems = cart.getCartItems()
                .stream()
                .mapToInt(item -> item.getQuantity())
                .sum();

        return CartResponse.builder()
                .cartId(cart.getId())
                .customerId(cart.getCustomer().getId())
                .totalItems(totalItems)
                .totalAmount(
                        cart.getTotalAmount()
                                .setScale(2, RoundingMode.HALF_UP)
                )
                .items(items)
                .build();
    }
}