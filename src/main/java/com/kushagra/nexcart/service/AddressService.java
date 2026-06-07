package com.kushagra.nexcart.service;

import java.util.List;

import com.kushagra.nexcart.dto.request.CreateAddressRequest;
import com.kushagra.nexcart.dto.request.UpdateAddressRequest;
import com.kushagra.nexcart.dto.response.AddressResponse;

public interface AddressService {

    AddressResponse createAddress(
            CreateAddressRequest request
    );

    List<AddressResponse> getMyAddresses();

    AddressResponse getAddressById(Long addressId);

    AddressResponse updateAddress(
            Long addressId,
            UpdateAddressRequest request
    );

    void deleteAddress(Long addressId);

    AddressResponse setDefaultAddress(Long addressId);

    AddressResponse getMyDefaultAddress();
}