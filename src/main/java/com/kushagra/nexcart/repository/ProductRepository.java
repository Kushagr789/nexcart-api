package com.kushagra.nexcart.repository;

import com.kushagra.nexcart.entity.Product;
import com.kushagra.nexcart.entity.User;
import com.kushagra.nexcart.enums.ProductStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    // SELLER PRODUCTS
    Page<Product> findByStoreOwner(
            User owner,
            Pageable pageable
    );

    // ACTIVE PRODUCTS
    Page<Product> findByStatus(
            ProductStatus status,
            Pageable pageable
    );

    // SEARCH
    Page<Product>
    findByNameContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );

    // CATEGORY FILTER
    Page<Product>
    findByCategories_Id(
            Long categoryId,
            Pageable pageable
    );

    Page<Product> findByStoreId(
            Long storeId,
            Pageable pageable
    );

    Page<Product> findByStoreSlug(
            String slug,
            Pageable pageable
    );

    Page<Product> findByStoreOwnerAndStatus(
            User owner,
            ProductStatus status,
            Pageable pageable
    );
}