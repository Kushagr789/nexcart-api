package com.kushagra.nexcart.controller;

import com.kushagra.nexcart.dto.request.ProductRequest;
import com.kushagra.nexcart.dto.response.ProductResponse;
import com.kushagra.nexcart.enums.ProductStatus;
import com.kushagra.nexcart.service.ProductService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // =========================
    // SELLER APIs
    // =========================

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid
            @RequestBody
            ProductRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        productService.createProduct(request)
                );
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,

            @Valid
            @RequestBody
            ProductRequest request
    ) {

        return ResponseEntity.ok(
                productService.updateProduct(
                        productId,
                        request
                )
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId
    ) {

        productService.deleteProduct(productId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{productId}/seller-status")
    public ResponseEntity<ProductResponse>
    updateSellerProductStatus(

            @PathVariable
            Long productId,

            @RequestParam
            ProductStatus status
    ) {

        return ResponseEntity.ok(
                productService.updateSellerProductStatus(
                        productId,
                        status
                )
        );
    }

    // =========================
    // ADMIN APIs
    // =========================

    @PatchMapping("/{productId}/admin-status")
    public ResponseEntity<ProductResponse>
    updateAdminProductStatus(

            @PathVariable
            Long productId,

            @RequestParam
            ProductStatus status
    ) {

        return ResponseEntity.ok(
                productService.updateAdminProductStatus(
                        productId,
                        status
                )
        );
    }

    // =========================
    // PUBLIC APIs
    // =========================



    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse>
    getProductById(
            @PathVariable Long productId
    ) {

        return ResponseEntity.ok(
                productService.getProductById(
                        productId
                )
        );
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>>
    getAllActiveProducts(

            @PageableDefault(
                    size = 10,
                    sort = "createdAt"
            )
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                productService.getAllActiveProducts(
                        pageable
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>>
    searchProducts(

            @RequestParam
            String keyword,

            @PageableDefault(
                    size = 10,
                    sort = "createdAt"
            )
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                productService.searchProducts(
                        keyword,
                        pageable
                )
        );
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponse>>
    getProductsByCategory(

            @PathVariable
            Long categoryId,

            @PageableDefault(
                    size = 10,
                    sort = "createdAt"
            )
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                productService.getProductsByCategory(
                        categoryId,
                        pageable
                )
        );
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<Page<ProductResponse>>
    getProductsByStore(

            @PathVariable
            Long storeId,

            @PageableDefault(
                    size = 10,
                    sort = "createdAt"
            )
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                productService.getProductsByStore(
                        storeId,
                        pageable
                )
        );
    }
}