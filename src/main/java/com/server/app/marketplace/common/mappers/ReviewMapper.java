package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.request.CreateReviewRequest;
import com.server.app.marketplace.domain.dto.response.review.ReviewResponse;
import com.server.app.marketplace.domain.entities.Product;
import com.server.app.marketplace.domain.entities.Review;
import com.server.app.marketplace.domain.entities.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReviewMapper {

    public Review toEntity(CreateReviewRequest request, User buyer, Product product) {
        return Review.builder()
                .buyer(buyer)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public ReviewResponse toDto(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .buyerId(review.getBuyer().getId())
                .buyerName(review.getBuyer().getFullName())
                .productId(review.getProduct().getId())
                .productTitle(review.getProduct().getTitle())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}