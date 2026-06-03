package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.request.CreateCouponRequest;
import com.server.app.marketplace.domain.dto.response.coupon.CouponResponse;
import com.server.app.marketplace.domain.entities.Category;
import com.server.app.marketplace.domain.entities.Coupon;
import com.server.app.marketplace.domain.entities.SellerProfile;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public Coupon toEntity(
            CreateCouponRequest request,
            SellerProfile sellerProfile,
            Category category
    ) {
        return Coupon.builder()
                .code(request.getCode().trim().toUpperCase())
                .discountPercentage(request.getDiscountPercentage())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .active(true)
                .sellerProfile(sellerProfile)
                .category(category)
                .build();
    }

    public CouponResponse toDto(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .discountPercentage(coupon.getDiscountPercentage())
                .startDate(coupon.getStartDate())
                .endDate(coupon.getEndDate())
                .active(coupon.getActive())
                .sellerProfileId(
                        coupon.getSellerProfile() != null
                                ? coupon.getSellerProfile().getId()
                                : null
                )
                .sellerStoreName(
                        coupon.getSellerProfile() != null
                                ? coupon.getSellerProfile().getStoreName()
                                : null
                )
                .categoryId(
                        coupon.getCategory() != null
                                ? coupon.getCategory().getId()
                                : null
                )
                .categoryName(
                        coupon.getCategory() != null
                                ? coupon.getCategory().getName()
                                : null
                )
                .build();
    }
}