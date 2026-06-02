package com.kushagra.nexcart.repository;

import com.kushagra.nexcart.entity.Store;
import com.kushagra.nexcart.entity.User;
import com.kushagra.nexcart.enums.StoreStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository
        extends JpaRepository<Store, Long> {

    // FIND STORE BY SLUG
    Optional<Store> findBySlug(String slug);

    // CHECK UNIQUE STORE NAME
    boolean existsByName(String name);

    // CHECK UNIQUE SLUG
    boolean existsBySlug(String slug);

    // GET ALL STORES OF A SELLER
    List<Store> findByOwner(User owner);

    // GET ALL STORES BY STATUS
    List<Store> findByStoreStatus(
            StoreStatus storeStatus
    );

    // FIND STORE BY ID AND OWNER
    Optional<Store> findByIdAndOwner(
            Long id,
            User owner
    );
}