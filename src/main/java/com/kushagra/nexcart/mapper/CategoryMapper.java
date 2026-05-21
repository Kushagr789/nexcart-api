package com.kushagra.nexcart.mapper;

import com.kushagra.nexcart.dto.request.CategoryRequest;
import com.kushagra.nexcart.dto.response.CategoryResponse;
import com.kushagra.nexcart.entity.Category;

public class CategoryMapper {

    // DTO -> ENTITY
    public static Category toEntity(
            CategoryRequest request
    ) {

        Category category = new Category();

        category.setName(request.getName());

        category.setDescription(
                request.getDescription()
        );

        return category;
    }

    // ENTITY -> DTO
    public static CategoryResponse toResponse(
            Category category
    ) {

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(
                        category.getDescription()
                )
                .parentCategoryId(
                        category.getParentCategory() != null
                                ? category.getParentCategory().getId()
                                : null
                )
                .parentCategoryName(
                        category.getParentCategory() != null
                                ? category.getParentCategory().getName()
                                : null
                )
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}