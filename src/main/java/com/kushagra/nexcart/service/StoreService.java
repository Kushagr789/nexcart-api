package com.kushagra.nexcart.service;

import com.kushagra.nexcart.dto.request.StoreRequest;
import com.kushagra.nexcart.dto.response.StoreResponse;
import com.kushagra.nexcart.entity.User;

import java.util.List;

public interface StoreService {

    StoreResponse createStore(
            StoreRequest request
    );

    StoreResponse getStoreById(
            Long storeId
    );

    StoreResponse getStoreBySlug(
            String slug
    );

    List<StoreResponse> getAllStores();

    List<StoreResponse> getMyStores(
    );

    List<StoreResponse> getStoresBySeller(
            Long sellerId
    );

    StoreResponse updateStore(
            Long storeId,
            StoreRequest request
    );
}