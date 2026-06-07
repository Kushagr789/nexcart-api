package com.kushagra.nexcart.service.impl;

import java.util.List;

import com.kushagra.nexcart.service.UserAuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kushagra.nexcart.dto.request.CreateAddressRequest;
import com.kushagra.nexcart.dto.request.UpdateAddressRequest;
import com.kushagra.nexcart.dto.response.AddressResponse;
import com.kushagra.nexcart.entity.Address;
import com.kushagra.nexcart.entity.User;
import com.kushagra.nexcart.exception.ResourceNotFoundException;
import com.kushagra.nexcart.mapper.AddressMapper;
import com.kushagra.nexcart.repository.AddressRepository;
import com.kushagra.nexcart.service.AddressService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserAuthService userAuthService;
    private final AddressMapper addressMapper;

    @Override
    public AddressResponse createAddress(
            CreateAddressRequest request
    ) {

        User user = userAuthService.getAuthenticatedUser();

        List<Address> existingAddresses =
                addressRepository.findByUser(user);

        boolean isFirstAddress =
                existingAddresses.isEmpty();

        if (Boolean.TRUE.equals(request.getIsDefault())
                || isFirstAddress) {

            clearDefaultAddresses(user);
        }

        Address address = addressMapper.toEntity(
                request,
                user
        );

        if (isFirstAddress) {
            address.setIsDefault(true);
        }

        Address savedAddress =
                addressRepository.save(address);

        return addressMapper.toResponse(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getMyAddresses() {

        User user = userAuthService.getAuthenticatedUser();

        return addressRepository.findByUser(user)
                .stream()
                .map(addressMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getAddressById(
            Long addressId
    ) {

        User user = userAuthService.getAuthenticatedUser();

        Address address =
                addressRepository.findByIdAndUser(
                        addressId,
                        user
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"
                        )
                );

        return addressMapper.toResponse(address);
    }

    @Override
    public AddressResponse updateAddress(
            Long addressId,
            UpdateAddressRequest request
    ) {

        User user = userAuthService.getAuthenticatedUser();

        Address address =
                addressRepository.findByIdAndUser(
                        addressId,
                        user
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"
                        )
                );

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            clearDefaultAddresses(user);
            address.setIsDefault(true);
        }

        addressMapper.updateEntity(address, request);

        Address updatedAddress =
                addressRepository.save(address);

        return addressMapper.toResponse(updatedAddress);
    }

    @Override
    public void deleteAddress(Long addressId) {

        User user = userAuthService.getAuthenticatedUser();

        Address address =
                addressRepository.findByIdAndUser(
                        addressId,
                        user
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"
                        )
                );

        boolean wasDefault =
                Boolean.TRUE.equals(address.getIsDefault());

        addressRepository.delete(address);

        if (wasDefault) {

            List<Address> remainingAddresses =
                    addressRepository.findByUser(user);

            if (!remainingAddresses.isEmpty()) {

                Address newDefault =
                        remainingAddresses.get(0);

                newDefault.setIsDefault(true);

                addressRepository.save(newDefault);
            }
        }
    }

    @Override
    public AddressResponse setDefaultAddress(
            Long addressId
    ) {

        User user = userAuthService.getAuthenticatedUser();

        Address address =
                addressRepository.findByIdAndUser(
                        addressId,
                        user
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"
                        )
                );

        clearDefaultAddresses(user);

        address.setIsDefault(true);

        Address updatedAddress =
                addressRepository.save(address);

        return addressMapper.toResponse(updatedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getMyDefaultAddress() {

        User user = userAuthService.getAuthenticatedUser();

        Address address = addressRepository
                .findByUserAndIsDefaultTrue(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Default address not found"
                        )
                );

        return addressMapper.toResponse(address);
    }

    // ================= HELPER METHODS =================

    private void clearDefaultAddresses(User user) {

        List<Address> addresses =
                addressRepository.findByUser(user);

        addresses.forEach(address ->
                address.setIsDefault(false)
        );

        addressRepository.saveAll(addresses);
    }
}