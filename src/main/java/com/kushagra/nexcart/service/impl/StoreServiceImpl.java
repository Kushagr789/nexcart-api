package com.kushagra.nexcart.service.impl;

import com.kushagra.nexcart.dto.request.StoreRequest;
import com.kushagra.nexcart.dto.response.StoreResponse;
import com.kushagra.nexcart.entity.Store;
import com.kushagra.nexcart.entity.User;
import com.kushagra.nexcart.enums.RoleName;
import com.kushagra.nexcart.enums.StoreStatus;
import com.kushagra.nexcart.exception.BadRequestException;
import com.kushagra.nexcart.exception.ResourceNotFoundException;
import com.kushagra.nexcart.mapper.StoreMapper;
import com.kushagra.nexcart.repository.StoreRepository;
import com.kushagra.nexcart.repository.UserRepository;
import com.kushagra.nexcart.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreServiceImpl
        implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final UserRepository userRepository;


    @Override
    public StoreResponse createStore(
            StoreRequest request,
            User currentUser
    ) {
        validateSeller(currentUser);

        if (storeRepository.existsByName(
                request.getName()
        )) {
            throw new BadRequestException(
                    "Store name already exists"
            );
        }

        String slug = generateUniqueSlug(
                request.getName()
        );

        Store store = storeMapper.toEntity(
                request,
                currentUser
        );

        store.setSlug(slug);

        store.setStoreStatus(
                StoreStatus.PENDING
        );

        Store savedStore =
                storeRepository.save(store);

        return storeMapper.toResponse(
                savedStore
        );
    }

    @Override
    public StoreResponse getStoreById(
            Long storeId
    ) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Store not found with id: "
                                        + storeId
                        )
                );

        return storeMapper.toResponse(store);
    }

    @Override
    public StoreResponse getStoreBySlug(
            String slug
    ) {
        Store store = storeRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Store not found with slug: "
                                        + slug
                        )
                );

        return storeMapper.toResponse(store);
    }

    @Override
    public List<StoreResponse> getAllStores() {
        return storeRepository.findAll()
                .stream()
                .map(storeMapper::toResponse)
                .toList();
    }

    @Override
    public List<StoreResponse> getMyStores(
            User currentUser
    ) {
        return storeRepository.findByOwner(currentUser)
                .stream()
                .map(storeMapper::toResponse)
                .toList();
    }

    @Override
    public List<StoreResponse> getStoresBySeller(
            Long sellerId
    ) {

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Seller not found with id: "
                                        + sellerId
                        )
                );

        return storeRepository.findByOwner(seller)
                .stream()
                .map(storeMapper::toResponse)
                .toList();
    }

    @Override
    public StoreResponse updateStore(
            Long storeId,
            StoreRequest request,
            User currentUser
    ) {
        Store store = storeRepository
                .findByIdAndOwner(
                        storeId,
                        currentUser
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Store not found or you do not own it"
                        )
                );

        if (!store.getName()
                .equalsIgnoreCase(
                        request.getName()
                )) {

            if (storeRepository.existsByName(
                    request.getName()
            )) {

                throw new BadRequestException(
                        "Store name already exists"
                );
            }

            store.setSlug(
                    generateUniqueSlug(
                            request.getName()
                    )
            );
        }

        storeMapper.updateEntity(
                store,
                request
        );

        store.setStoreStatus(
                StoreStatus.PENDING
        );
        Store updatedStore =
                storeRepository.save(store);

        return storeMapper.toResponse(
                updatedStore
        );
    }

    private void validateSeller(
            User currentUser
    ) {
        boolean isSeller = currentUser.getRoles()
                .stream()
                .anyMatch(role ->
                        role.getName()
                                == RoleName.ROLE_SELLER
                );

        if (!isSeller) {

            throw new BadRequestException(
                    "Only sellers can create stores"
            );
        }
    }

    private String generateUniqueSlug(
            String storeName
    ) {

        String baseSlug = slugify(storeName);

        String slug = baseSlug;

        int count = 1;

        while (storeRepository.existsBySlug(
                slug
        )) {

            slug = baseSlug + "-" + count;

            count++;
        }

        return slug;
    }

    //SLUGIFY STORE NAME
    private String slugify(
            String input
    ) {

        return Normalizer.normalize(
                        input,
                        Normalizer.Form.NFD
                )
                .replaceAll("[^\\w\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }
}
