package com.server.app.marketplace.repositories;

import com.server.app.marketplace.domain.entities.SellerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerProfileRepository extends JpaRepository<SellerProfile, Long> {

    boolean existsByStoreNameIgnoreCase(String storeName);

    boolean existsByUserId(Long userId);

    Optional<SellerProfile> findByUserId(Long userId);
}