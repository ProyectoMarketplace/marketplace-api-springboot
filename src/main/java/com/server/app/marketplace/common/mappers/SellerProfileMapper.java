package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.request.SellerProfileRequest;
import com.server.app.marketplace.domain.dto.response.seller.SellerProfileResponse;
import com.server.app.marketplace.domain.entities.SellerProfile;
import com.server.app.marketplace.domain.entities.User;
import org.springframework.stereotype.Component;

@Component
public class SellerProfileMapper {

    public SellerProfile toEntity(SellerProfileRequest request, User user) {
        return SellerProfile.builder()
                .storeName(request.getStoreName())
                .identityVerified(false)
                .commissionRate(0.10)
                .user(user)
                .build();
    }

    public void updateEntity(SellerProfile profile, SellerProfileRequest request) {
        profile.setStoreName(request.getStoreName());
    }

    public SellerProfileResponse toDto(SellerProfile profile) {
        return SellerProfileResponse.builder()
                .id(profile.getId())
                .storeName(profile.getStoreName())
                .identityVerified(profile.getIdentityVerified())
                .commissionRate(profile.getCommissionRate())
                .userId(profile.getUser().getId())
                .sellerName(profile.getUser().getFullName())
                .email(profile.getUser().getEmail())
                .build();
    }
}