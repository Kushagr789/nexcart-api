package com.kushagra.nexcart.service;

import com.kushagra.nexcart.dto.request.ProductRequest;
import com.kushagra.nexcart.dto.response.ProductResponse;
import com.kushagra.nexcart.enums.ProductStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponse createProduct(
            ProductRequest request
    );

    ProductResponse updateProduct(
            Long productId,
            ProductRequest request
    );

    void deleteProduct(Long productId);

    ProductResponse getProductById(Long productId);

    // PUBLIC PRODUCTS
    Page<ProductResponse> getAllActiveProducts(
            Pageable pageable
    );

    Page<ProductResponse> searchProducts(
            String keyword,
            Pageable pageable
    );

    Page<ProductResponse> getProductsByCategory(
            Long categoryId,
            Pageable pageable
    );

    Page<ProductResponse> getProductsByStore(
            Long storeId,
            Pageable pageable
    );

    ProductResponse updateSellerProductStatus(
            Long productId,
            ProductStatus status
    );

    ProductResponse updateAdminProductStatus(
            Long productId,
            ProductStatus status
    );
}