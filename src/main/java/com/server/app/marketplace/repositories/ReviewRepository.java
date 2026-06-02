package com.server.app.marketplace.repositories;

import com.server.app.marketplace.domain.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long productId);

    boolean existsByBuyerIdAndProductId(Long buyerId, Long productId);
}