package com.kushagra.nexcart.controller;

import com.kushagra.nexcart.dto.request.AddToCartRequest;
import com.kushagra.nexcart.dto.request.UpdateCartItemRequest;
import com.kushagra.nexcart.dto.response.CartResponse;
import com.kushagra.nexcart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart APIs", description = "APIs for customer cart management")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    @Operation(summary = "Add product to cart")
    public ResponseEntity<CartResponse> addItem(
            @Valid
            @RequestBody AddToCartRequest request
    ) {
        return ResponseEntity.ok(
                cartService.addItem(request)
        );
    }

    @GetMapping
    @Operation(summary = "Get logged-in customer's cart")
    public ResponseEntity<CartResponse> getCartItems(
    ) {
        return ResponseEntity.ok(
                cartService.getMyCart()
        );
    }

    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update cart item quantity")
    public ResponseEntity<CartResponse> updateQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {

        return ResponseEntity.ok(
                cartService.updateQuantity(
                        cartItemId,
                        request
                )
        );
    }

    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Remove item from cart")
    public ResponseEntity<String> removeItem(
            @PathVariable Long cartItemId
    ) {

        cartService.removeItem(cartItemId);

        return ResponseEntity.ok(
                "Item removed from cart successfully"
        );
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear logged-in customer's cart")
    public ResponseEntity<String> clearCart() {

        cartService.clearCart();

        return ResponseEntity.ok(
                "Cart cleared successfully"
        );
    }
}
