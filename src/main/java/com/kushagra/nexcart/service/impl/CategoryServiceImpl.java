package com.kushagra.nexcart.service.impl;

import com.kushagra.nexcart.dto.request.CategoryRequest;
import com.kushagra.nexcart.dto.response.CategoryResponse;
import com.kushagra.nexcart.entity.Category;
import com.kushagra.nexcart.exception.ResourceAlreadyExistsException;
import com.kushagra.nexcart.exception.ResourceNotFoundException;
import com.kushagra.nexcart.mapper.CategoryMapper;
import com.kushagra.nexcart.repository.CategoryRepository;
import com.kushagra.nexcart.service.CategoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl
        implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(
            CategoryRequest request
    ) {

        // DUPLICATE NAME VALIDATION
        if (categoryRepository.existsByName(
                request.getName()
        )) {

            throw new ResourceAlreadyExistsException(
                    "Category already exists with name: "
                            + request.getName()
            );
        }

        // MAP DTO -> ENTITY
        Category category =
                CategoryMapper.toEntity(request);

        // HANDLE PARENT CATEGORY
        if (request.getParentCategoryId()
                != null) {

            Category parentCategory =
                    categoryRepository.findById(
                                    request.getParentCategoryId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Parent category not found with id: "
                                                    + request.getParentCategoryId()
                                    )
                            );

            category.setParentCategory(
                    parentCategory
            );
        }

        // SAVE
        Category savedCategory =
                categoryRepository.save(category);

        // ENTITY -> DTO
        return CategoryMapper.toResponse(
                savedCategory
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(
            Long categoryId
    ) {

        Category category =
                categoryRepository.findById(categoryId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found with id: "
                                                + categoryId
                                )
                        );

        return CategoryMapper.toResponse(
                category
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse>
    getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse>
    getSubCategories(Long parentCategoryId) {

        return categoryRepository
                .findByParentCategoryId(
                        parentCategoryId
                )
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse updateCategory(
            Long categoryId,
            CategoryRequest request
    ) {

        Category existingCategory =
                categoryRepository.findById(categoryId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found with id: "
                                                + categoryId
                                )
                        );

        // CHECK DUPLICATE NAME
        if (!existingCategory.getName()
                .equalsIgnoreCase(
                        request.getName()
                )
                &&
                categoryRepository.existsByName(
                        request.getName()
                )) {

            throw new ResourceAlreadyExistsException(
                    "Category already exists with name: "
                            + request.getName()
            );
        }

        // UPDATE FIELDS
        existingCategory.setName(
                request.getName()
        );

        existingCategory.setDescription(
                request.getDescription()
        );

        // HANDLE PARENT CATEGORY
        if (request.getParentCategoryId()
                != null) {

            // PREVENT SELF PARENTING
            if (categoryId.equals(
                    request.getParentCategoryId()
            )) {

                throw new IllegalArgumentException(
                        "Category cannot be parent of itself"
                );
            }

            Category parentCategory =
                    categoryRepository.findById(
                                    request.getParentCategoryId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Parent category not found with id: "
                                                    + request.getParentCategoryId()
                                    )
                            );

            existingCategory.setParentCategory(
                    parentCategory
            );

        } else {

            existingCategory.setParentCategory(
                    null
            );
        }

        Category updatedCategory =
                categoryRepository.save(
                        existingCategory
                );

        return CategoryMapper.toResponse(
                updatedCategory
        );
    }

    @Override
    public void deleteCategory(Long categoryId) {

        Category category =
                categoryRepository.findById(categoryId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found with id: "
                                                + categoryId
                                )
                        );

        categoryRepository.delete(category);
    }
}