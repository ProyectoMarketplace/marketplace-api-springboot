package com.server.app.marketplace.repositories;

import com.server.app.marketplace.domain.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByOrderId(Long orderId);

    Optional<Payment> findByOrderId(Long orderId);
}