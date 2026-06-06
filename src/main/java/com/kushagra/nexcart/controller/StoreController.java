package com.kushagra.nexcart.controller;

import com.kushagra.nexcart.dto.request.StoreRequest;
import com.kushagra.nexcart.dto.response.StoreResponse;
import com.kushagra.nexcart.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    //CREATE
    @PreAuthorize("hasRole('SELLER')")
    @PostMapping
    public ResponseEntity<StoreResponse>
    createStore(
            @Valid
            @RequestBody
            StoreRequest request
    ) {
        StoreResponse response =
                storeService.createStore(
                        request
                );
        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    // GET STORE BY ID
    @GetMapping("/id/{storeId:\\d+}")
    public ResponseEntity<StoreResponse>
    getStoreById(
            @PathVariable Long storeId
    ) {

        StoreResponse response =
                storeService.getStoreById(
                        storeId
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slug:[a-zA-Z0-9-]+}")
    public ResponseEntity<StoreResponse> getStoreBySlug(
            @PathVariable String slug
    ) {

        return ResponseEntity.ok(
                storeService.getStoreBySlug(slug)
        );
    }

    // GET ALL STORES
    @GetMapping
    public ResponseEntity<List<StoreResponse>>
    getAllStores() {

        List<StoreResponse> response =
                storeService.getAllStores();

        return ResponseEntity.ok(response);
    }

    // GET MY(Seller's) STORES
    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/myStores")
    public ResponseEntity<List<StoreResponse>>
    getMyStores(
    ) {

        List<StoreResponse> response =
                storeService.getMyStores();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/seller/{sellerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StoreResponse>> getStoresBySeller(
            @PathVariable Long sellerId
    ) {

        return ResponseEntity.ok(
                storeService.getStoresBySeller(
                        sellerId
                )
        );
    }

    //UPDATE STORE
    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResponse>
    updateStore(
            @PathVariable Long storeId,

            @Valid
            @RequestBody
            StoreRequest request
    ) {
        StoreResponse response =
                storeService.updateStore(
                        storeId,
                        request
                );
        return ResponseEntity.ok(response);
    }

}
