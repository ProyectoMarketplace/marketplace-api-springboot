package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.mappers.CouponMapper;
import com.server.app.marketplace.domain.dto.request.CreateCouponRequest;
import com.server.app.marketplace.domain.dto.response.coupon.CouponResponse;
import com.server.app.marketplace.domain.entities.Category;
import com.server.app.marketplace.domain.entities.Coupon;
import com.server.app.marketplace.domain.entities.SellerProfile;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.CategoryRepository;
import com.server.app.marketplace.repositories.CouponRepository;
import com.server.app.marketplace.repositories.SellerProfileRepository;
import com.server.app.marketplace.services.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    private final SellerProfileRepository sellerProfileRepository;

    private final CategoryRepository categoryRepository;

    private final CouponMapper couponMapper;

    @Override
    @Transactional
    public CouponResponse createCoupon(CreateCouponRequest request) {
        if (couponRepository.existsByCodeIgnoreCase(request.getCode())) {
            throw new BusinessRuleException("Coupon code is already registered.");
        }

        validateDates(request);

        if (request.getSellerProfileId() == null && request.getCategoryId() == null) {
            throw new BusinessRuleException("Coupon must belong to a seller or a category.");
        }

        if (request.getSellerProfileId() != null && request.getCategoryId() != null) {
            throw new BusinessRuleException("Coupon cannot belong to seller and category at the same time.");
        }

        SellerProfile sellerProfile = null;
        Category category = null;

        if (request.getSellerProfileId() != null) {
            sellerProfile = sellerProfileRepository.findById(request.getSellerProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seller profile not found."));
        }

        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

            if (!category.getActive()) {
                throw new BusinessRuleException("Category is not active.");
            }
        }

        Coupon coupon = couponMapper.toEntity(request, sellerProfile, category);
        Coupon savedCoupon = couponRepository.save(coupon);

        return couponMapper.toDto(savedCoupon);
    }

    @Override
    public List<CouponResponse> getActiveCoupons() {
        return couponRepository.findByActiveTrueAndEndDateGreaterThanEqual(LocalDate.now())
                .stream()
                .map(couponMapper::toDto)
                .toList();
    }

    @Override
    public CouponResponse getCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found."));

        return couponMapper.toDto(coupon);
    }

    @Override
    @Transactional
    public CouponResponse deactivateCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found."));

        coupon.setActive(false);

        Coupon updatedCoupon = couponRepository.save(coupon);

        return couponMapper.toDto(updatedCoupon);
    }

    private void validateDates(CreateCouponRequest request) {
        if (request.getStartDate().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("Start date cannot be in the past.");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BusinessRuleException("End date cannot be before start date.");
        }
    }
}