package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.response.promotion.FeaturedPromotionResponse;
import com.server.app.marketplace.domain.entities.FeaturedPromotion;
import org.springframework.stereotype.Component;

@Component
public class FeaturedPromotionMapper {

    public FeaturedPromotionResponse toDto(FeaturedPromotion promotion) {
        return FeaturedPromotionResponse.builder()
                .id(promotion.getId())
                .productId(promotion.getProduct().getId())
                .productTitle(promotion.getProduct().getTitle())
                .productPrice(promotion.getProduct().getPrice())
                .productStatus(promotion.getProduct().getStatus())
                .sellerUserId(promotion.getSeller().getId())
                .sellerStoreName(promotion.getProduct().getSellerProfile().getStoreName())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .visibilityFee(promotion.getVisibilityFee())
                .paid(promotion.getPaid())
                .status(promotion.getStatus())
                .displayOrder(promotion.getDisplayOrder())
                .createdAt(promotion.getCreatedAt())
                .updatedAt(promotion.getUpdatedAt())
                .build();
    }
}
