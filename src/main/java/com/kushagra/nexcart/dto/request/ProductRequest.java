package com.kushagra.nexcart.dto.request;

import jakarta.validation.constraints.*;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 200)
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 3000)
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stockQuantity;

    @Size(max = 100)
    private String brand;

    @Size(max = 500)
    private String imageUrl;

    @NotEmpty(message = "At least one category is required")
    private Set<Long> categoryIds;

    @NotNull(message = "Store id is required")
    private Long storeId;
}