package com.kushagra.nexcart.repository;

import com.kushagra.nexcart.entity.Address;
import com.kushagra.nexcart.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository
        extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);

    Optional<Address> findByIdAndUser(
            Long id,
            User user
    );

    Optional<Address> findByUserAndIsDefaultTrue(
            User user
    );
}