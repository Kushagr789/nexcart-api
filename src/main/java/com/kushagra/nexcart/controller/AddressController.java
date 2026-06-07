package com.kushagra.nexcart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import com.kushagra.nexcart.dto.request.CreateAddressRequest;
import com.kushagra.nexcart.dto.request.UpdateAddressRequest;
import com.kushagra.nexcart.dto.response.AddressResponse;
import com.kushagra.nexcart.service.AddressService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Tag(
        name = "Address APIs",
        description = "APIs for customer address management"
)
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('CUSTOMER')")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    @Operation(summary = "Create new address")
    public ResponseEntity<AddressResponse> createAddress(
            @Valid @RequestBody CreateAddressRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        addressService.createAddress(request)
                );
    }

    @GetMapping
    @Operation(summary = "Get logged-in customer's addresses")
    public ResponseEntity<List<AddressResponse>> getMyAddresses() {

        return ResponseEntity.ok(
                addressService.getMyAddresses()
        );
    }

    @GetMapping("/default")
    @Operation(summary = "Get logged-in customer's default address")
    public ResponseEntity<AddressResponse> getMyDefaultAddress() {

        return ResponseEntity.ok(
                addressService.getMyDefaultAddress()
        );
    }

    @GetMapping("/{addressId}")
    @Operation(summary = "Get address by id")
    public ResponseEntity<AddressResponse> getAddressById(
            @PathVariable Long addressId
    ) {

        return ResponseEntity.ok(
                addressService.getAddressById(addressId)
        );
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Update address")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody UpdateAddressRequest request
    ) {

        return ResponseEntity.ok(
                addressService.updateAddress(
                        addressId,
                        request
                )
        );
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Delete address")
    public ResponseEntity<String> deleteAddress(
            @PathVariable Long addressId
    ) {

        addressService.deleteAddress(addressId);

        return ResponseEntity.ok(
                "Address deleted successfully"
        );
    }

    @PatchMapping("/{addressId}/default")
    @Operation(summary = "Set default address")
    public ResponseEntity<AddressResponse> setDefaultAddress(
            @PathVariable Long addressId
    ) {
        return ResponseEntity.ok(
                addressService.setDefaultAddress(addressId)
        );
    }
}