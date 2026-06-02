package com.server.app.marketplace.repositories;

import com.server.app.marketplace.domain.entities.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    boolean existsByOrderId(Long orderId);

    Optional<Shipment> findByOrderId(Long orderId);
}