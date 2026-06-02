package com.server.app.marketplace.repositories;

import com.server.app.marketplace.domain.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByBuyerId(Long buyerId);
}