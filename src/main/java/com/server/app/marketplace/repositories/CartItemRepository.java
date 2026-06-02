package com.server.app.marketplace.repositories;

import com.server.app.marketplace.domain.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByIdAndCartBuyerId(Long id, Long buyerId);
}