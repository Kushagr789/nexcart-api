package com.kushagra.nexcart.controller;

import com.kushagra.nexcart.dto.request.CheckoutRequest;
import com.kushagra.nexcart.dto.request.UpdateOrderStatusRequest;
import com.kushagra.nexcart.dto.response.OrderResponse;
import com.kushagra.nexcart.dto.response.OrderSummaryResponse;
import com.kushagra.nexcart.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order APIs", description = "APIs for Order Managment")
public class OrderController {

    private final OrderService orderService;

    // ================= CUSTOMER =================

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(
            summary = "Checkout cart",
            description = "Creates an order from the customer's active cart"
    )
    public ResponseEntity<OrderResponse> checkout(
            @Valid @RequestBody CheckoutRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.checkout(request));
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(
            summary = "Get my orders",
            description = "Returns all orders of the authenticated customer"
    )
    public ResponseEntity<List<OrderSummaryResponse>> getMyOrders() {

        return ResponseEntity.ok(
                orderService.getMyOrders()
        );
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(
            summary = "Get order details",
            description = "Returns details of a specific customer order"
    )
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                orderService.getOrderById(orderId)
        );
    }

    @PatchMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(
            summary = "Cancel order",
            description = "Cancels an order if it is eligible for cancellation"
    )
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                orderService.cancelOrder(orderId)
        );
    }

    // ================= ADMIN =================

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update order status",
            description = "Allows an admin to update the status of an order"
    )
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(
                        orderId,
                        request.getOrderStatus()
                )
        );
    }
}