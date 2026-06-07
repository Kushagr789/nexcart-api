package com.kushagra.nexcart.mapper;

import com.kushagra.nexcart.dto.request.UpdateAddressRequest;
import org.springframework.stereotype.Component;

import com.kushagra.nexcart.dto.request.CreateAddressRequest;
import com.kushagra.nexcart.dto.response.AddressResponse;
import com.kushagra.nexcart.entity.Address;
import com.kushagra.nexcart.entity.User;

@Component
public class AddressMapper {

    public Address toEntity(
            CreateAddressRequest request,
            User user
    ) {

        return Address.builder()
                .user(user)
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .street(request.getStreet())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())
                .isDefault(
                        Boolean.TRUE.equals(
                                request.getIsDefault()
                        )
                )
                .build();
    }

    public void updateEntity(
            Address address,
            UpdateAddressRequest request
    ) {

        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
    }

    public AddressResponse toResponse(Address address) {

        return AddressResponse.builder()
                .id(address.getId())
                .fullName(address.getFullName())
                .phoneNumber(address.getPhoneNumber())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .isDefault(address.getIsDefault())
                .build();
    }
}