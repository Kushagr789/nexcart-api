package com.kushagra.nexcart.entity;

import com.kushagra.nexcart.enums.ProductStatus;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            length = 200
    )
    private String name;

    @Column(
            nullable = false,
            length = 3000
    )
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(length = 100)
    private String brand;

    @Column(length = 500)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    // MULTIPLE CATEGORIES
    @ManyToMany
    @JoinTable(
            name = "product_categories",
            joinColumns =
            @JoinColumn(name = "product_id"),

            inverseJoinColumns =
            @JoinColumn(name = "category_id")
    )
    private Set<Category> categories =
            new HashSet<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
