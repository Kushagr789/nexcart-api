package com.kushagra.nexcart.mapper;

import com.kushagra.nexcart.dto.response.CategoryResponse;
import com.kushagra.nexcart.dto.response.ProductResponse;
import com.kushagra.nexcart.entity.Category;
import com.kushagra.nexcart.entity.Product;

import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductResponse toResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .brand(product.getBrand())
                .imageUrl(product.getImageUrl())
                .status(product.getStatus())

                // Store
                .storeId(product.getStore().getId())
                .storeName(product.getStore().getName())

                // Categories
                .categories(
                        product.getCategories()
                                .stream()
                                .map(ProductMapper::mapCategory)
                                .collect(Collectors.toSet())
                )

                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private static CategoryResponse mapCategory(Category category) {

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .updatedAt(category.getUpdatedAt())
                .createdAt(category.getCreatedAt())
                .build();
    }
}