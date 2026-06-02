package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.enums.ProductStatus;
import com.server.app.marketplace.common.enums.UserRole;
import com.server.app.marketplace.common.mappers.ReviewMapper;
import com.server.app.marketplace.domain.dto.request.CreateReviewRequest;
import com.server.app.marketplace.domain.dto.response.review.ReviewResponse;
import com.server.app.marketplace.domain.entities.Product;
import com.server.app.marketplace.domain.entities.Review;
import com.server.app.marketplace.domain.entities.User;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.ProductRepository;
import com.server.app.marketplace.repositories.ReviewRepository;
import com.server.app.marketplace.repositories.UserRepository;
import com.server.app.marketplace.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewResponse createReview(CreateReviewRequest request) {
        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));

        if (buyer.getRole() != UserRole.BUYER) {
            throw new BusinessRuleException("Only BUYER users can create reviews.");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        if (product.getStatus() != ProductStatus.APPROVED) {
            throw new BusinessRuleException("Only approved products can be reviewed.");
        }

        if (reviewRepository.existsByBuyerIdAndProductId(request.getBuyerId(), request.getProductId())) {
            throw new BusinessRuleException("This buyer has already reviewed this product.");
        }

        Review review = reviewMapper.toEntity(request, buyer, product);
        Review savedReview = reviewRepository.save(review);

        return reviewMapper.toDto(savedReview);
    }

    @Override
    public List<ReviewResponse> getReviewsByProductId(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        return reviewRepository.findByProductId(productId)
                .stream()
                .map(reviewMapper::toDto)
                .toList();
    }
}