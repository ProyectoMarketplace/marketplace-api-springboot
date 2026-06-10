package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.enums.ProductStatus;
import com.server.app.marketplace.common.enums.UserRole;
import com.server.app.marketplace.common.mappers.PriceNotificationMapper;
import com.server.app.marketplace.domain.dto.request.CreatePriceAlertRequest;
import com.server.app.marketplace.domain.dto.response.price.PriceAlertResponse;
import com.server.app.marketplace.domain.dto.response.price.PriceNotificationResponse;
import com.server.app.marketplace.domain.entities.PriceAlert;
import com.server.app.marketplace.domain.entities.PriceNotification;
import com.server.app.marketplace.domain.entities.Product;
import com.server.app.marketplace.domain.entities.User;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.PriceAlertRepository;
import com.server.app.marketplace.repositories.PriceNotificationRepository;
import com.server.app.marketplace.repositories.ProductRepository;
import com.server.app.marketplace.repositories.UserRepository;
import com.server.app.marketplace.services.PriceNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceNotificationServiceImpl implements PriceNotificationService {

    private final PriceAlertRepository priceAlertRepository;
    private final PriceNotificationRepository priceNotificationRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PriceNotificationMapper priceNotificationMapper;

    @Override
    @Transactional
    public PriceAlertResponse createPriceAlert(CreatePriceAlertRequest request) {
        User buyer = findActiveUser(request.getBuyerId());
        requireRole(buyer, UserRole.BUYER, "Only BUYER users can create price alerts.");

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        if (product.getStatus() != ProductStatus.APPROVED) {
            throw new BusinessRuleException("Price alerts can only be created for APPROVED products.");
        }

        if (priceAlertRepository.existsByBuyerIdAndProductIdAndActiveTrue(
                buyer.getId(),
                product.getId()
        )) {
            throw new BusinessRuleException("This buyer already has an active price alert for this product.");
        }

        if (request.getTargetPrice() != null) {
            if (request.getTargetPrice() >= product.getPrice()) {
                throw new BusinessRuleException("Target price must be lower than the current product price.");
            }
        }

        PriceAlert alert = PriceAlert.builder()
                .buyer(buyer)
                .product(product)
                .targetPrice(request.getTargetPrice())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        return priceNotificationMapper.toAlertDto(priceAlertRepository.save(alert));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceAlertResponse> getAlertsByBuyer(Long buyerId) {
        User buyer = findActiveUser(buyerId);
        requireRole(buyer, UserRole.BUYER, "Only BUYER users can view price alerts.");

        return priceAlertRepository.findByBuyerIdAndActiveTrueOrderByCreatedAtDesc(buyerId)
                .stream()
                .map(priceNotificationMapper::toAlertDto)
                .toList();
    }

    @Override
    @Transactional
    public PriceAlertResponse deactivateAlert(Long alertId, Long buyerId) {
        User buyer = findActiveUser(buyerId);
        requireRole(buyer, UserRole.BUYER, "Only BUYER users can deactivate price alerts.");

        PriceAlert alert = findAlert(alertId);

        if (!alert.getBuyer().getId().equals(buyer.getId())) {
            throw new BusinessRuleException("This price alert does not belong to the buyer.");
        }

        if (!Boolean.TRUE.equals(alert.getActive())) {
            throw new BusinessRuleException("This price alert is already inactive.");
        }

        alert.setActive(false);
        return priceNotificationMapper.toAlertDto(priceAlertRepository.save(alert));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceNotificationResponse> getNotificationsByBuyer(Long buyerId) {
        User buyer = findActiveUser(buyerId);
        requireRole(buyer, UserRole.BUYER, "Only BUYER users can view price notifications.");

        return priceNotificationRepository.findByBuyerIdOrderByCreatedAtDesc(buyerId)
                .stream()
                .map(priceNotificationMapper::toNotificationDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceNotificationResponse> getUnreadNotificationsByBuyer(Long buyerId) {
        User buyer = findActiveUser(buyerId);
        requireRole(buyer, UserRole.BUYER, "Only BUYER users can view price notifications.");

        return priceNotificationRepository.findByBuyerIdAndReadFalseOrderByCreatedAtDesc(buyerId)
                .stream()
                .map(priceNotificationMapper::toNotificationDto)
                .toList();
    }

    @Override
    @Transactional
    public PriceNotificationResponse markNotificationAsRead(Long notificationId, Long buyerId) {
        User buyer = findActiveUser(buyerId);
        requireRole(buyer, UserRole.BUYER, "Only BUYER users can mark notifications as read.");

        PriceNotification notification = findNotification(notificationId);

        if (!notification.getBuyer().getId().equals(buyer.getId())) {
            throw new BusinessRuleException("This notification does not belong to the buyer.");
        }

        notification.setRead(true);
        return priceNotificationMapper.toNotificationDto(priceNotificationRepository.save(notification));
    }

    @Override
    @Transactional
    public void processPriceDrop(Product product, Double previousPrice, Double newPrice) {
        if (newPrice >= previousPrice) {
            return;
        }

        List<PriceAlert> alerts = priceAlertRepository.findByProductIdAndActiveTrue(product.getId());
        LocalDateTime now = LocalDateTime.now();

        for (PriceAlert alert : alerts) {
            if (alert.getTargetPrice() != null && newPrice > alert.getTargetPrice()) {
                continue;
            }

            PriceNotification notification = PriceNotification.builder()
                    .buyer(alert.getBuyer())
                    .product(product)
                    .previousPrice(previousPrice)
                    .newPrice(newPrice)
                    .message(buildMessage(product.getTitle(), previousPrice, newPrice, alert.getTargetPrice()))
                    .read(false)
                    .createdAt(now)
                    .build();

            priceNotificationRepository.save(notification);
        }
    }

    private String buildMessage(
            String productTitle,
            Double previousPrice,
            Double newPrice,
            Double targetPrice
    ) {
        if (targetPrice != null) {
            return String.format(
                    "Price dropped on %s from %.2f to %.2f (target: %.2f).",
                    productTitle,
                    previousPrice,
                    newPrice,
                    targetPrice
            );
        }

        return String.format(
                "Price dropped on %s from %.2f to %.2f.",
                productTitle,
                previousPrice,
                newPrice
        );
    }

    private PriceAlert findAlert(Long id) {
        return priceAlertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Price alert not found."));
    }

    private PriceNotification findNotification(Long id) {
        return priceNotificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Price notification not found."));
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
