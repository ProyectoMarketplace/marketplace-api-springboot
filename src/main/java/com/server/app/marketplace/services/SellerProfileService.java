package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.SellerProfileRequest;
import com.server.app.marketplace.domain.dto.response.seller.SellerProfileResponse;

public interface SellerProfileService {

    SellerProfileResponse createProfile(Long userId, SellerProfileRequest request);

    SellerProfileResponse getProfileByUserId(Long userId);

    SellerProfileResponse updateProfile(Long userId, SellerProfileRequest request);
}