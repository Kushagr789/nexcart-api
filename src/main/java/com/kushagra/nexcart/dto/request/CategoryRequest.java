package com.kushagra.nexcart.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(
            min = 2,
            max = 100,
            message =
                    "Category name must be between 2 and 100 characters"
    )
    private String name;

    @Size(
            max = 500,
            message =
                    "Description cannot exceed 500 characters"
    )
    private String description;

    // Null = root category
    private Long parentCategoryId;
}