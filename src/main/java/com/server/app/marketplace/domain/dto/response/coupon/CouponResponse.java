package com.server.app.marketplace.domain.dto.response.coupon;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponse {

    private Long id;

    private String code;

    private Double discountPercentage;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean active;

    private Long sellerProfileId;

    private String sellerStoreName;

    private Long categoryId;

    private String categoryName;
}