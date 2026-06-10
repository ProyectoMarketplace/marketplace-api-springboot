package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.CreatePriceAlertRequest;
import com.server.app.marketplace.domain.dto.response.price.PriceAlertResponse;
import com.server.app.marketplace.domain.dto.response.price.PriceNotificationResponse;
import com.server.app.marketplace.domain.entities.Product;

import java.util.List;

public interface PriceNotificationService {

    PriceAlertResponse createPriceAlert(CreatePriceAlertRequest request);

    List<PriceAlertResponse> getAlertsByBuyer(Long buyerId);

    PriceAlertResponse deactivateAlert(Long alertId, Long buyerId);

    List<PriceNotificationResponse> getNotificationsByBuyer(Long buyerId);

    List<PriceNotificationResponse> getUnreadNotificationsByBuyer(Long buyerId);

    PriceNotificationResponse markNotificationAsRead(Long notificationId, Long buyerId);

    void processPriceDrop(Product product, Double previousPrice, Double newPrice);
}
