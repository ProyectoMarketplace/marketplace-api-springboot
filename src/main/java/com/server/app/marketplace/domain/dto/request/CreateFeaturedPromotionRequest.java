package com.server.app.marketplace.domain.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateFeaturedPromotionRequest {

    @NotNull(message = "Seller user id is required.")
    private Long sellerUserId;

    @NotNull(message = "Product id is required.")
    private Long productId;

    @NotNull(message = "Start date is required.")
    private LocalDate startDate;

    @NotNull(message = "End date is required.")
    private LocalDate endDate;

    @NotNull(message = "Visibility fee is required.")
    @DecimalMin(value = "5.0", message = "Visibility fee must be at least 5.0.")
    private Double visibilityFee;
}
