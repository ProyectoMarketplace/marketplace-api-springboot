package com.server.app.marketplace.repositories;

import com.server.app.marketplace.domain.entities.PriceNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceNotificationRepository extends JpaRepository<PriceNotification, Long> {

    List<PriceNotification> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);

    List<PriceNotification> findByBuyerIdAndReadFalseOrderByCreatedAtDesc(Long buyerId);
}
