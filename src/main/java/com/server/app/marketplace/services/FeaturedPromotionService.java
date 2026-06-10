package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.CancelFeaturedPromotionRequest;
import com.server.app.marketplace.domain.dto.request.CreateFeaturedPromotionRequest;
import com.server.app.marketplace.domain.dto.request.PayFeaturedPromotionRequest;
import com.server.app.marketplace.domain.dto.response.promotion.FeaturedPromotionResponse;

import java.util.List;

public interface FeaturedPromotionService {

    FeaturedPromotionResponse createPromotion(CreateFeaturedPromotionRequest request);

    FeaturedPromotionResponse getPromotionById(Long id);

    List<FeaturedPromotionResponse> getActiveFeaturedPromotions();

    List<FeaturedPromotionResponse> getPromotionsBySeller(Long sellerUserId);

    List<FeaturedPromotionResponse> getAllPromotionsForAdmin(Long adminUserId);

    FeaturedPromotionResponse payForPromotion(Long id, PayFeaturedPromotionRequest request);

    FeaturedPromotionResponse cancelPromotion(Long id, CancelFeaturedPromotionRequest request);
}
