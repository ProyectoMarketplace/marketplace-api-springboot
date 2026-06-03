package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.CreateCouponRequest;
import com.server.app.marketplace.domain.dto.response.coupon.CouponResponse;

import java.util.List;

public interface CouponService {

    CouponResponse createCoupon(CreateCouponRequest request);

    List<CouponResponse> getActiveCoupons();

    CouponResponse getCouponByCode(String code);

    CouponResponse deactivateCoupon(Long id);
}