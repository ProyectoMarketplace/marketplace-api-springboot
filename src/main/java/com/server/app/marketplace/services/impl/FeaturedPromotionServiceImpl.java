package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.enums.FeaturedPromotionStatus;
import com.server.app.marketplace.common.enums.ProductStatus;
import com.server.app.marketplace.common.enums.UserRole;
import com.server.app.marketplace.common.mappers.FeaturedPromotionMapper;
import com.server.app.marketplace.domain.dto.request.CancelFeaturedPromotionRequest;
import com.server.app.marketplace.domain.dto.request.CreateFeaturedPromotionRequest;
import com.server.app.marketplace.domain.dto.request.PayFeaturedPromotionRequest;
import com.server.app.marketplace.domain.dto.response.promotion.FeaturedPromotionResponse;
import com.server.app.marketplace.domain.entities.FeaturedPromotion;
import com.server.app.marketplace.domain.entities.Product;
import com.server.app.marketplace.domain.entities.User;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.FeaturedPromotionRepository;
import com.server.app.marketplace.repositories.ProductRepository;
import com.server.app.marketplace.repositories.UserRepository;
import com.server.app.marketplace.services.FeaturedPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FeaturedPromotionServiceImpl implements FeaturedPromotionService {

    private static final int MAX_PROMOTION_DAYS = 30;
    private static final double MIN_DAILY_FEE = 5.0;

    private static final Set<FeaturedPromotionStatus> BLOCKING_STATUSES = Set.of(
            FeaturedPromotionStatus.PENDING_PAYMENT,
            FeaturedPromotionStatus.ACTIVE
    );

    private final FeaturedPromotionRepository featuredPromotionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final FeaturedPromotionMapper featuredPromotionMapper;

    @Override
    @Transactional
    public FeaturedPromotionResponse createPromotion(CreateFeaturedPromotionRequest request) {
        User seller = findActiveUser(request.getSellerUserId());
        requireRole(seller, UserRole.SELLER, "Only SELLER users can create featured promotions.");

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        if (!product.getSellerProfile().getUser().getId().equals(seller.getId())) {
            throw new BusinessRuleException("This product does not belong to the seller.");
        }

        if (product.getStatus() != ProductStatus.APPROVED) {
            throw new BusinessRuleException("Only APPROVED products can be featured.");
        }

        validateDates(request.getStartDate(), request.getEndDate());
        validateVisibilityFee(request.getStartDate(), request.getEndDate(), request.getVisibilityFee());

        if (featuredPromotionRepository.existsByProductIdAndStatusIn(
                product.getId(),
                BLOCKING_STATUSES
        )) {
            throw new BusinessRuleException("This product already has an active or pending featured promotion.");
        }

        LocalDateTime now = LocalDateTime.now();

        FeaturedPromotion promotion = FeaturedPromotion.builder()
                .product(product)
                .seller(seller)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .visibilityFee(request.getVisibilityFee())
                .paid(false)
                .status(FeaturedPromotionStatus.PENDING_PAYMENT)
                .displayOrder(calculateDisplayOrder(request.getVisibilityFee()))
                .createdAt(now)
                .updatedAt(now)
                .build();

        return featuredPromotionMapper.toDto(featuredPromotionRepository.save(promotion));
    }

    @Override
    @Transactional(readOnly = true)
    public FeaturedPromotionResponse getPromotionById(Long id) {
        return featuredPromotionMapper.toDto(findPromotion(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeaturedPromotionResponse> getActiveFeaturedPromotions() {
        LocalDate today = LocalDate.now();

        return featuredPromotionRepository
                .findByPaidTrueAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByDisplayOrderDesc(
                        FeaturedPromotionStatus.ACTIVE,
                        today,
                        today
                )
                .stream()
                .map(featuredPromotionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeaturedPromotionResponse> getPromotionsBySeller(Long sellerUserId) {
        User seller = findActiveUser(sellerUserId);
        requireRole(seller, UserRole.SELLER, "Only SELLER users can view seller promotions.");

        return featuredPromotionRepository.findBySellerIdOrderByCreatedAtDesc(sellerUserId)
                .stream()
                .map(featuredPromotionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeaturedPromotionResponse> getAllPromotionsForAdmin(Long adminUserId) {
        User admin = findActiveUser(adminUserId);
        requireRole(admin, UserRole.ADMIN, "Only ADMIN users can view all featured promotions.");

        return featuredPromotionRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(featuredPromotionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public FeaturedPromotionResponse payForPromotion(Long id, PayFeaturedPromotionRequest request) {
        User seller = findActiveUser(request.getSellerUserId());
        requireRole(seller, UserRole.SELLER, "Only SELLER users can pay for featured promotions.");

        FeaturedPromotion promotion = findPromotion(id);

        if (!promotion.getSeller().getId().equals(seller.getId())) {
            throw new BusinessRuleException("This promotion does not belong to the seller.");
        }

        if (promotion.getStatus() != FeaturedPromotionStatus.PENDING_PAYMENT) {
            throw new BusinessRuleException("Only promotions pending payment can be paid.");
        }

        if (!Boolean.TRUE.equals(request.getApproved())) {
            promotion.setStatus(FeaturedPromotionStatus.CANCELLED);
            promotion.setUpdatedAt(LocalDateTime.now());
            return featuredPromotionMapper.toDto(featuredPromotionRepository.save(promotion));
        }

        if (promotion.getEndDate().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("Promotion end date has already passed.");
        }

        promotion.setPaid(true);
        promotion.setStatus(FeaturedPromotionStatus.ACTIVE);
        promotion.setUpdatedAt(LocalDateTime.now());

        return featuredPromotionMapper.toDto(featuredPromotionRepository.save(promotion));
    }

    @Override
    @Transactional
    public FeaturedPromotionResponse cancelPromotion(Long id, CancelFeaturedPromotionRequest request) {
        User user = findActiveUser(request.getRequestingUserId());
        FeaturedPromotion promotion = findPromotion(id);

        boolean isOwner = promotion.getSeller().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == UserRole.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new BusinessRuleException("Only the seller owner or an ADMIN can cancel this promotion.");
        }

        if (promotion.getStatus() == FeaturedPromotionStatus.CANCELLED
                || promotion.getStatus() == FeaturedPromotionStatus.EXPIRED) {
            throw new BusinessRuleException("This promotion is already closed.");
        }

        promotion.setStatus(FeaturedPromotionStatus.CANCELLED);
        promotion.setUpdatedAt(LocalDateTime.now());

        return featuredPromotionMapper.toDto(featuredPromotionRepository.save(promotion));
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(LocalDate.now())) {
            throw new BusinessRuleException("Start date cannot be in the past.");
        }

        if (endDate.isBefore(startDate)) {
            throw new BusinessRuleException("End date cannot be before start date.");
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (days > MAX_PROMOTION_DAYS) {
            throw new BusinessRuleException("Featured promotions cannot exceed 30 days.");
        }
    }

    private void validateVisibilityFee(LocalDate startDate, LocalDate endDate, Double visibilityFee) {
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        double minimumFee = days * MIN_DAILY_FEE;

        if (visibilityFee < minimumFee) {
            throw new BusinessRuleException(
                    "Visibility fee must be at least " + minimumFee + " for the selected period."
            );
        }
    }

    private int calculateDisplayOrder(Double visibilityFee) {
        return visibilityFee.intValue();
    }

    private FeaturedPromotion findPromotion(Long id) {
        return featuredPromotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Featured promotion not found."));
    }

    private User findActiveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BusinessRuleException("User is inactive.");
        }

        return user;
    }

    private void requireRole(User user, UserRole role, String message) {
        if (user.getRole() != role) {
            throw new BusinessRuleException(message);
        }
    }
}
