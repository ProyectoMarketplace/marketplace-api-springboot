package com.server.app.marketplace.domain.dto.response.review;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

    private Long id;

    private Long buyerId;

    private String buyerName;

    private Long productId;

    private String productTitle;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;
}