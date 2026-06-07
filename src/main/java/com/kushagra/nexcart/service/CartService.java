package com.kushagra.nexcart.service;

import com.kushagra.nexcart.dto.request.AddToCartRequest;
import com.kushagra.nexcart.dto.request.UpdateCartItemRequest;
import com.kushagra.nexcart.dto.response.CartResponse;

public interface CartService {

    CartResponse addItem(AddToCartRequest request);

    CartResponse getMyCart();

    CartResponse updateQuantity(
            Long cartItemId,
            UpdateCartItemRequest request
    );

    void removeItem(Long cartItemId);

    void clearCart();
}