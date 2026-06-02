package com.kushagra.nexcart.dto.response;

import com.kushagra.nexcart.enums.StoreStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StoreResponse {

    private Long id;

    private String name;

    private String slug;

    private String description;

    private String logoUrl;

    private StoreStatus storeStatus;

    private Long ownerId;

    private String ownerName;
}