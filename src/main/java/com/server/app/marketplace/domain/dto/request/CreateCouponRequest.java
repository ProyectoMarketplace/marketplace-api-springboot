package com.server.app.marketplace.domain.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCouponRequest {

    @NotBlank(message = "Coupon code is required.")
    private String code;

    @NotNull(message = "Discount percentage is required.")
    @DecimalMin(value = "1.0", message = "Discount percentage must be at least 1%.")
    @DecimalMax(value = "90.0", message = "Discount percentage cannot be greater than 90%.")
    private Double discountPercentage;

    @NotNull(message = "Start date is required.")
    private LocalDate startDate;

    @NotNull(message = "End date is required.")
    private LocalDate endDate;

    private Long sellerProfileId;

    private Long categoryId;
}