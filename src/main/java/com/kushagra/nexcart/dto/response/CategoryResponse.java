package com.kushagra.nexcart.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class CategoryResponse {

    private Long id;

    private String name;

    private String description;

    private Long parentCategoryId;

    private String parentCategoryName;

    private Instant createdAt;

    private Instant updatedAt;
}