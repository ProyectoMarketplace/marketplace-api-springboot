package com.server.app.marketplace.repositories;

import com.server.app.marketplace.common.enums.FeaturedPromotionStatus;
import com.server.app.marketplace.domain.entities.FeaturedPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface FeaturedPromotionRepository extends JpaRepository<FeaturedPromotion, Long> {

    List<FeaturedPromotion> findBySellerIdOrderByCreatedAtDesc(Long sellerId);

    List<FeaturedPromotion> findAllByOrderByCreatedAtDesc();

    List<FeaturedPromotion> findByPaidTrueAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByDisplayOrderDesc(
            FeaturedPromotionStatus status,
            LocalDate startDate,
            LocalDate endDate
    );

    boolean existsByProductIdAndStatusIn(Long productId, Collection<FeaturedPromotionStatus> statuses);
}
