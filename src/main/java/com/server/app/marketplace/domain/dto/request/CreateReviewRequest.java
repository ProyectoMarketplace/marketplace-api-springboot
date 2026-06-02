package com.server.app.marketplace.domain.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewRequest {

    @NotNull(message = "Buyer id is required.")
    private Long buyerId;

    @NotNull(message = "Product id is required.")
    private Long productId;

    @NotNull(message = "Rating is required.")
    @Min(value = 1, message = "Rating must be at least 1.")
    @Max(value = 5, message = "Rating cannot be greater than 5.")
    private Integer rating;

    private String comment;
}