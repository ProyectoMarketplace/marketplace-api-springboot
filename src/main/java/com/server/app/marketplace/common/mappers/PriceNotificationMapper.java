package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.response.price.PriceAlertResponse;
import com.server.app.marketplace.domain.dto.response.price.PriceNotificationResponse;
import com.server.app.marketplace.domain.entities.PriceAlert;
import com.server.app.marketplace.domain.entities.PriceNotification;
import org.springframework.stereotype.Component;

@Component
public class PriceNotificationMapper {

    public PriceAlertResponse toAlertDto(PriceAlert alert) {
        return PriceAlertResponse.builder()
                .id(alert.getId())
                .buyerId(alert.getBuyer().getId())
                .productId(alert.getProduct().getId())
                .productTitle(alert.getProduct().getTitle())
                .currentPrice(alert.getProduct().getPrice())
                .targetPrice(alert.getTargetPrice())
                .active(alert.getActive())
                .createdAt(alert.getCreatedAt())
                .build();
    }

    public PriceNotificationResponse toNotificationDto(PriceNotification notification) {
        return PriceNotificationResponse.builder()
                .id(notification.getId())
                .buyerId(notification.getBuyer().getId())
                .productId(notification.getProduct().getId())
                .productTitle(notification.getProduct().getTitle())
                .previousPrice(notification.getPreviousPrice())
                .newPrice(notification.getNewPrice())
                .message(notification.getMessage())
                .read(notification.getRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
