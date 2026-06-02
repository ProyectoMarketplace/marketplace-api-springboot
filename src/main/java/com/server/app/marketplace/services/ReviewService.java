package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.CreateReviewRequest;
import com.server.app.marketplace.domain.dto.response.review.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse createReview(CreateReviewRequest request);

    List<ReviewResponse> getReviewsByProductId(Long productId);
}