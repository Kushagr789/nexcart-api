package com.kushagra.nexcart.service;

import com.kushagra.nexcart.dto.request.CategoryRequest;
import com.kushagra.nexcart.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(
            CategoryRequest request
    );

    CategoryResponse getCategoryById(
            Long categoryId
    );

    List<CategoryResponse> getAllCategories();

    List<CategoryResponse> getSubCategories(
            Long parentCategoryId
    );

    CategoryResponse updateCategory(
            Long categoryId,
            CategoryRequest request
    );

    void deleteCategory(Long categoryId);
}