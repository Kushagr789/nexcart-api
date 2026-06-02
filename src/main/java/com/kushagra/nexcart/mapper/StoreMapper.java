package com.kushagra.nexcart.mapper;

import com.kushagra.nexcart.dto.request.StoreRequest;
import com.kushagra.nexcart.dto.response.StoreResponse;
import com.kushagra.nexcart.entity.Store;
import com.kushagra.nexcart.entity.User;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    // REQUEST DTO -> ENTITY
    public Store toEntity(
            StoreRequest request,
            User owner
    ) {

        return Store.builder()
                .name(request.getName())
                .description(request.getDescription())
                .logoUrl(request.getLogoUrl())
                .owner(owner)
                .build();
    }

    // ENTITY -> RESPONSE DTO
    public StoreResponse toResponse(
            Store store
    ) {

        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .slug(store.getSlug())
                .description(store.getDescription())
                .logoUrl(store.getLogoUrl())
                .storeStatus(store.getStoreStatus())
                .ownerId(store.getOwner().getId())
                .ownerName(
                        store.getOwner().getFirstName()
                                + " "
                                + store.getOwner().getLastName()
                )
                .build();
    }

    // UPDATE EXISTING ENTITY
    public void updateEntity(
            Store store,
            StoreRequest request
    ) {

        store.setName(request.getName());
        store.setDescription(request.getDescription());
        store.setLogoUrl(request.getLogoUrl());
    }
}