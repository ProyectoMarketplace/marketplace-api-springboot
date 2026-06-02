package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.CreateReviewRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/api/reviews")
    public ResponseEntity<GeneralResponse> createReview(
            @Valid @RequestBody CreateReviewRequest request
    ) {
        return buildResponse(
                "Review created successfully.",
                HttpStatus.CREATED,
                reviewService.createReview(request)
        );
    }

    @GetMapping("/api/products/{productId}/reviews")
    public ResponseEntity<GeneralResponse> getReviewsByProductId(@PathVariable Long productId) {
        return buildResponse(
                "Reviews found.",
                HttpStatus.OK,
                reviewService.getReviewsByProductId(productId)
        );
    }

    public ResponseEntity<GeneralResponse> buildResponse(
            String message,
            HttpStatus status,
            Object data
    ) {
        String uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .build()
                .getPath();

        return ResponseEntity
                .status(status)
                .body(GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(LocalDateTime.now())
                        .data(data)
                        .build());
    }
}