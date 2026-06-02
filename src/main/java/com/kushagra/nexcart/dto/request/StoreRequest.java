package com.kushagra.nexcart.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequest {

    @NotBlank(message = "Store name is required")
    @Size(min = 3, max = 100,
            message = "Store name must be between 3 and 100 characters")
    private String name;

    @Size(max = 1000,
            message = "Description cannot exceed 1000 characters")
    private String description;

    private String logoUrl;
}