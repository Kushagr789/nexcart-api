package com.kushagra.nexcart.service.impl;

import com.kushagra.nexcart.dto.request.ProductRequest;
import com.kushagra.nexcart.dto.response.ProductResponse;
import com.kushagra.nexcart.entity.Category;
import com.kushagra.nexcart.entity.Product;
import com.kushagra.nexcart.entity.Store;
import com.kushagra.nexcart.entity.User;
import com.kushagra.nexcart.enums.ProductStatus;
import com.kushagra.nexcart.exception.BadRequestException;
import com.kushagra.nexcart.exception.ResourceNotFoundException;
import com.kushagra.nexcart.mapper.ProductMapper;
import com.kushagra.nexcart.repository.CategoryRepository;
import com.kushagra.nexcart.repository.ProductRepository;
import com.kushagra.nexcart.repository.StoreRepository;
import com.kushagra.nexcart.service.ProductService;
import com.kushagra.nexcart.service.UserAuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl
        implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final StoreRepository storeRepository;

    private final UserAuthService userAuthService;

    @Override
    public ProductResponse createProduct(
            ProductRequest request
    ) {

        User seller =
                userAuthService.getAuthenticatedUser();

        userAuthService.validateSeller(seller);

        Store store = getValidatedStore(
                request.getStoreId(),
                seller
        );

        Set<Category> categories =
                getValidatedCategories(
                        request.getCategoryIds()
                );

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .brand(request.getBrand())
                .imageUrl(request.getImageUrl())
                .status(ProductStatus.DRAFT)
                .store(store)
                .categories(categories)
                .build();

        Product savedProduct =
                productRepository.save(product);

        return ProductMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(
            Long productId,
            ProductRequest request
    ) {

        User seller =
                userAuthService.getAuthenticatedUser();

        userAuthService.validateSeller(seller);

        Product product = getProductOrThrow(productId);

        validateProductOwnership(
                product,
                seller
        );

        Set<Category> categories =
                getValidatedCategories(
                        request.getCategoryIds()
                );

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setBrand(request.getBrand());
        product.setImageUrl(request.getImageUrl());
        product.setCategories(categories);

        Product updatedProduct =
                productRepository.save(product);

        return ProductMapper.toResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(
            Long productId
    ) {

        User seller =
                userAuthService.getAuthenticatedUser();

        userAuthService.validateSeller(seller);

        Product product =
                getProductOrThrow(productId);

        validateProductOwnership(
                product,
                seller
        );

        productRepository.delete(product);
    }

    @Override
    public ProductResponse getProductById(
            Long productId
    ) {

        Product product =
                getProductOrThrow(productId);

        return ProductMapper.toResponse(product);
    }

    @Override
    public Page<ProductResponse> getAllActiveProducts(
            Pageable pageable
    ) {

        return productRepository
                .findByStatus(
                        ProductStatus.ACTIVE,
                        pageable
                )
                .map(ProductMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> searchProducts(
            String keyword,
            Pageable pageable
    ) {

        return productRepository
                .findByNameContainingIgnoreCaseAndStatus(
                        keyword,
                        ProductStatus.ACTIVE,
                        pageable
                )
                .map(ProductMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByCategory(
            Long categoryId,
            Pageable pageable
    ) {

        return productRepository
                .findByCategories_IdAndStatus(
                        categoryId,
                        ProductStatus.ACTIVE,
                        pageable
                )
                .map(ProductMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByStore(
            Long storeId,
            Pageable pageable
    ) {

        return productRepository
                .findByStoreIdAndStatus(
                        storeId,
                        ProductStatus.ACTIVE,
                        pageable
                )
                .map(ProductMapper::toResponse);
    }

    @Override
    public ProductResponse updateSellerProductStatus(
            Long productId,
            ProductStatus status
    ) {

        User seller =
                userAuthService.getAuthenticatedUser();

        userAuthService.validateSeller(seller);

        Product product =
                getProductOrThrow(productId);

        validateProductOwnership(
                product,
                seller
        );

        // SELLER CANNOT ACTIVATE OR BLOCK
        if (status == ProductStatus.ACTIVE
                || status == ProductStatus.BLOCKED) {

            throw new BadRequestException(
                    "You are not allowed to set this status"
            );
        }

        // BLOCKED PRODUCTS CANNOT BE MODIFIED
        if (product.getStatus()
                == ProductStatus.BLOCKED) {

            throw new BadRequestException(
                    "Blocked products cannot be modified"
            );
        }

        product.setStatus(status);

        Product updatedProduct =
                productRepository.save(product);

        return ProductMapper.toResponse(updatedProduct);
    }

    @Override
    public ProductResponse updateAdminProductStatus(
            Long productId,
            ProductStatus status
    ) {

        User admin =
                userAuthService.getAuthenticatedUser();

        userAuthService.validateAdmin(admin);

        Product product =
                getProductOrThrow(productId);

        product.setStatus(status);

        Product updatedProduct =
                productRepository.save(product);

        return ProductMapper.toResponse(updatedProduct);
    }

    // =========================
    // HELPER METHODS
    // =========================

    private Product getProductOrThrow(
            Long productId
    ) {

        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"
                        ));
    }

    private Store getValidatedStore(
            Long storeId,
            User seller
    ) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Store not found"
                        ));

        if (!store.getOwner().getId()
                .equals(seller.getId())) {

            throw new BadRequestException(
                    "You are not allowed to access this store"
            );
        }

        return store;
    }

    private Set<Category> getValidatedCategories(
            Set<Long> categoryIds
    ) {

        return categoryIds.stream()
                .map(categoryId ->
                        categoryRepository.findById(categoryId)
                                .orElseThrow(() ->
                                        new ResourceNotFoundException(
                                                "Category not found with id: "
                                                        + categoryId
                                        )))
                .collect(Collectors.toSet());
    }

    private void validateProductOwnership(
            Product product,
            User seller
    ) {

        if (!product.getStore()
                .getOwner()
                .getId()
                .equals(seller.getId())) {

            throw new BadRequestException(
                    "You are not allowed to access this product"
            );
        }
    }
}