package com.kushagra.nexcart.service.impl;

import com.kushagra.nexcart.dto.request.CheckoutRequest;
import com.kushagra.nexcart.dto.response.OrderResponse;
import com.kushagra.nexcart.dto.response.OrderSummaryResponse;
import com.kushagra.nexcart.entity.*;
import com.kushagra.nexcart.enums.CartStatus;
import com.kushagra.nexcart.enums.OrderStatus;
import com.kushagra.nexcart.enums.PaymentStatus;
import com.kushagra.nexcart.exception.BusinessException;
import com.kushagra.nexcart.exception.ResourceNotFoundException;
import com.kushagra.nexcart.mapper.OrderMapper;
import com.kushagra.nexcart.repository.*;
import com.kushagra.nexcart.service.OrderService;
import com.kushagra.nexcart.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final CartRepository cartRepository;

    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    private final UserAuthService userAuthService;

    private final OrderMapper orderMapper;

    @Override
    public OrderResponse checkout(CheckoutRequest request) {
        User customer = userAuthService.getAuthenticatedUser();
        Cart cart = cartRepository.findByCustomerAndStatus(
                customer,
                CartStatus.ACTIVE
        ).orElseThrow(() ->
            new ResourceNotFoundException(
                    "Cart not found!"
            )
        );

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "Cart is empty"
            );
        }

        Address shippingAddress =
                addressRepository.findByIdAndUser(
                                request.getShippingAddressId(),
                                customer
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Shipping address not found"
                                )
                        );

        Map<Long, Product> products =
                validateAndFetchProducts(cart);

        Order order =
                createOrder(
                        customer,
                        shippingAddress,
                        cart
                );

        createOrderItems(
                order,
                cart,
                products
        );

        order = orderRepository.save(order);
        reduceStock(cart, products);
        clearCart(cart);

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderSummaryResponse> getMyOrders() {

        User customer = userAuthService.getAuthenticatedUser();

        return orderRepository
                .findByCustomerOrderByCreatedAtDesc(customer)
                .stream()
                .map(orderMapper::toSummaryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {

        User customer = userAuthService.getAuthenticatedUser();

        Order order = orderRepository
                .findByIdAndCustomer(
                        orderId,
                        customer
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"
                        )
                );

        return orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse cancelOrder(Long orderId) {

        User customer = userAuthService.getAuthenticatedUser();

        Order order = orderRepository
                .findByIdAndCustomer(
                        orderId,
                        customer
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"
                        )
                );

        if (order.getOrderStatus() == OrderStatus.SHIPPED
                || order.getOrderStatus() == OrderStatus.DELIVERED
                || order.getOrderStatus() == OrderStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Order cannot be cancelled"
            );
        }

        order.setOrderStatus(
                OrderStatus.CANCELLED
        );

        orderRepository.save(order);

        return orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse updateOrderStatus(
            Long orderId,
            OrderStatus orderStatus
    ) {

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"
                        )
                );

        order.setOrderStatus(orderStatus);

        orderRepository.save(order);

        return orderMapper.toResponse(order);
    }

    private Map<Long, Product> validateAndFetchProducts(
            Cart cart
    ) {

        Map<Long, Product> products = new HashMap<>();

        for (CartItem cartItem : cart.getCartItems()) {

            Product product =
                    productRepository.findById(
                                    cartItem.getProduct().getId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Product not found"
                                    )
                            );

            if (product.getStockQuantity()
                    < cartItem.getQuantity()) {

                throw new BusinessException(
                        product.getName()
                                + " has insufficient stock"
                );
            }

            products.put(product.getId(), product);
        }

        return products;
    }

    private void reduceStock(
            Cart cart,
            Map<Long, Product> products
    ) {

        for (CartItem cartItem : cart.getCartItems()) {

            Product product =
                    products.get(
                            cartItem.getProduct().getId()
                    );

            product.setStockQuantity(
                    product.getStockQuantity()
                            - cartItem.getQuantity()
            );
        }

        productRepository.saveAll(
                products.values()
        );
    }

    private Order createOrder(
            User customer,
            Address shippingAddress,
            Cart cart
    ) {

        return Order.builder()
                .customer(customer)
                .shippingAddress(shippingAddress)
                .totalAmount(cart.getTotalAmount())
                .orderStatus(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .build();
    }

    private void createOrderItems(
            Order order,
            Cart cart,
            Map<Long, Product> products
    ) {

        for (CartItem cartItem : cart.getCartItems()) {

            Product product = products.get(
                    cartItem.getProduct().getId()
            );

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .productNameSnapshot(
                            product.getName()
                    )
                    .productPriceSnapshot(
                            product.getPrice()
                    )
                    .quantity(
                            cartItem.getQuantity()
                    )
                    .subtotal(
                            cartItem.getSubtotal()
                    )
                    .build();

            order.getOrderItems().add(orderItem);
        }
    }

    private void clearCart(Cart cart) {

        cart.getCartItems().clear();

        cart.setTotalAmount(BigDecimal.ZERO);

        cartRepository.save(cart);
    }
}
