package com.server.app.marketplace.repositories;

import com.server.app.marketplace.domain.entities.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {

    List<PriceAlert> findByBuyerIdAndActiveTrueOrderByCreatedAtDesc(Long buyerId);

    List<PriceAlert> findByProductIdAndActiveTrue(Long productId);

    boolean existsByBuyerIdAndProductIdAndActiveTrue(Long buyerId, Long productId);
}
