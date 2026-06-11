package com.kushagra.nexcart.service;

import java.util.List;

import com.kushagra.nexcart.dto.request.CheckoutRequest;
import com.kushagra.nexcart.dto.response.OrderResponse;
import com.kushagra.nexcart.dto.response.OrderSummaryResponse;
import com.kushagra.nexcart.enums.OrderStatus;

public interface OrderService {

    OrderResponse checkout(
            CheckoutRequest request
    );

    List<OrderSummaryResponse> getMyOrders();

    OrderResponse getOrderById(
            Long orderId
    );

    OrderResponse cancelOrder(
            Long orderId
    );

    OrderResponse updateOrderStatus(
            Long orderId,
            OrderStatus orderStatus
    );
}