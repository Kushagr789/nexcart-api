package com.kushagra.nexcart.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kushagra.nexcart.dto.request.AddToCartRequest;
import com.kushagra.nexcart.dto.request.UpdateCartItemRequest;
import com.kushagra.nexcart.dto.response.CartResponse;
import com.kushagra.nexcart.entity.Cart;
import com.kushagra.nexcart.entity.CartItem;
import com.kushagra.nexcart.entity.Product;
import com.kushagra.nexcart.entity.User;
import com.kushagra.nexcart.enums.CartStatus;
import com.kushagra.nexcart.enums.ProductStatus;
import com.kushagra.nexcart.exception.ResourceNotFoundException;
import com.kushagra.nexcart.mapper.CartMapper;
import com.kushagra.nexcart.repository.CartItemRepository;
import com.kushagra.nexcart.repository.CartRepository;
import com.kushagra.nexcart.repository.ProductRepository;
import com.kushagra.nexcart.repository.UserRepository;
import com.kushagra.nexcart.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Override
    public CartResponse addItem(AddToCartRequest request) {

        User customer = getAuthenticatedUser();

        Product product = productRepository.findById(
                request.getProductId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Product not found")
        );

        validateProduct(product);

        validateStock(
                product,
                request.getQuantity()
        );

        Cart cart = cartRepository.findByCustomerAndStatus(
                customer,
                CartStatus.ACTIVE
        ).orElseGet(() -> createNewCart(customer));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(
                cart,
                product
        ).orElse(null);

        if (cartItem != null) {

            int updatedQuantity =
                    cartItem.getQuantity() + request.getQuantity();

            validateStock(product, updatedQuantity);

            cartItem.setQuantity(updatedQuantity);

            cartItem.setPrice(
                    product.getPrice()
                            .setScale(2, RoundingMode.HALF_UP)
            );

            cartItem.setSubtotal(
                    calculateSubtotal(
                            cartItem.getPrice(),
                            updatedQuantity
                    )
            );

        } else {

            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .price(
                            product.getPrice()
                                    .setScale(2, RoundingMode.HALF_UP)
                    )
                    .subtotal(
                            calculateSubtotal(
                                    product.getPrice(),
                                    request.getQuantity()
                            )
                    )
                    .build();

            cart.getCartItems().add(cartItem);
        }

        recalculateCart(cart);

        cartItemRepository.save(cartItem);

        return cartMapper.toResponse(
                cartRepository.save(cart)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getMyCart() {

        User customer = getAuthenticatedUser();

        Cart cart = cartRepository.findByCustomerAndStatus(
                customer,
                CartStatus.ACTIVE
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Cart is empty"
                )
        );

        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse updateQuantity(
            Long cartItemId,
            UpdateCartItemRequest request
    ) {

        User customer = getAuthenticatedUser();

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found"
                        )
                );

        validateCartOwnership(customer, cartItem);

        Product product = cartItem.getProduct();

        validateProduct(product);

        validateStock(product, request.getQuantity());

        cartItem.setQuantity(request.getQuantity());

        cartItem.setPrice(
                product.getPrice()
                        .setScale(2, RoundingMode.HALF_UP)
        );

        cartItem.setSubtotal(
                calculateSubtotal(
                        cartItem.getPrice(),
                        request.getQuantity()
                )
        );

        Cart cart = cartItem.getCart();

        recalculateCart(cart);

        cartItemRepository.save(cartItem);

        return cartMapper.toResponse(
                cartRepository.save(cart)
        );
    }

    @Override
    public void removeItem(Long cartItemId) {

        User customer = getAuthenticatedUser();

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found"
                        )
                );

        validateCartOwnership(customer, cartItem);

        Cart cart = cartItem.getCart();

        cart.getCartItems().remove(cartItem);

        cartItemRepository.delete(cartItem);

        recalculateCart(cart);

        cartRepository.save(cart);
    }

    @Override
    public void clearCart() {

        User customer = getAuthenticatedUser();

        Cart cart = cartRepository.findByCustomerAndStatus(
                customer,
                CartStatus.ACTIVE
        ).orElseThrow(() ->
                new ResourceNotFoundException("Cart not found")
        );

        cart.getCartItems().clear();

        cart.setTotalAmount(BigDecimal.ZERO);

        cartRepository.save(cart);
    }

    // ================= HELPER METHODS =================

    private Cart createNewCart(User customer) {

        Cart cart = Cart.builder()
                .customer(customer)
                .status(CartStatus.ACTIVE)
                .totalAmount(BigDecimal.ZERO)
                .build();

        return cartRepository.save(cart);
    }

    private void validateProduct(Product product) {

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Product is not active"
            );
        }
    }

    private void validateStock(
            Product product,
            Integer requestedQuantity
    ) {

        if (product.getStockQuantity() < requestedQuantity) {
            throw new IllegalArgumentException(
                    "Insufficient stock available"
            );
        }
    }

    private void validateCartOwnership(
            User customer,
            CartItem cartItem
    ) {

        if (!cartItem.getCart()
                .getCustomer()
                .getId()
                .equals(customer.getId())) {

            throw new IllegalArgumentException(
                    "You cannot access another user's cart"
            );
        }
    }

    private BigDecimal calculateSubtotal(
            BigDecimal price,
            Integer quantity
    ) {

        return price.multiply(
                BigDecimal.valueOf(quantity)
        ).setScale(2, RoundingMode.HALF_UP);
    }

    private void recalculateCart(Cart cart) {

        BigDecimal total = cart.getCartItems()
                .stream()
                .map(CartItem::getSubtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        cart.setTotalAmount(
                total.setScale(2, RoundingMode.HALF_UP)
        );
    }

    private User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );
    }
}