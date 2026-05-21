package com.kushagra.nexcart.controller;

import com.kushagra.nexcart.dto.request.CategoryRequest;
import com.kushagra.nexcart.dto.response.CategoryResponse;
import com.kushagra.nexcart.service.CategoryService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // CREATE CATEGORY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponse>
    createCategory(
            @Valid
            @RequestBody
            CategoryRequest request
    ) {

        CategoryResponse response =
                categoryService.createCategory(
                        request
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    // GET CATEGORY BY ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse>
    getCategoryById(
            @PathVariable Long categoryId
    ) {

        CategoryResponse response =
                categoryService.getCategoryById(
                        categoryId
                );

        return ResponseEntity.ok(response);
    }

    // GET ALL CATEGORIES
    @GetMapping
    public ResponseEntity<List<CategoryResponse>>
    getAllCategories() {

        List<CategoryResponse> response =
                categoryService.getAllCategories();

        return ResponseEntity.ok(response);
    }

    // GET SUBCATEGORIES
    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<List<CategoryResponse>>
    getSubCategories(
            @PathVariable Long categoryId
    ) {

        List<CategoryResponse> response =
                categoryService.getSubCategories(
                        categoryId
                );

        return ResponseEntity.ok(response);
    }

    // UPDATE CATEGORY
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse>
    updateCategory(
            @PathVariable Long categoryId,

            @Valid
            @RequestBody
            CategoryRequest request
    ) {

        CategoryResponse response =
                categoryService.updateCategory(
                        categoryId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    // DELETE CATEGORY
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void>
    deleteCategory(
            @PathVariable Long categoryId
    ) {

        categoryService.deleteCategory(
                categoryId
        );

        return ResponseEntity.noContent().build();
    }
}