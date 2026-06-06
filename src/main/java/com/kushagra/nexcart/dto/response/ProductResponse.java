package com.kushagra.nexcart.dto.response;

import com.kushagra.nexcart.enums.ProductStatus;

import lombok.*;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Integer stockQuantity;

    private String brand;

    private String imageUrl;

    private ProductStatus status;

    // Store Info
    private Long storeId;

    private String storeName;

    // Categories
    private Set<CategoryResponse> categories;

    private Instant createdAt;

    private Instant updatedAt;
}