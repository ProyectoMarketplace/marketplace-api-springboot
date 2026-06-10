package com.server.app.marketplace.domain.dto.response.promotion;

import com.server.app.marketplace.common.enums.FeaturedPromotionStatus;
import com.server.app.marketplace.common.enums.ProductStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeaturedPromotionResponse {

    private Long id;

    private Long productId;

    private String productTitle;

    private Double productPrice;

    private ProductStatus productStatus;

    private Long sellerUserId;

    private String sellerStoreName;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double visibilityFee;

    private Boolean paid;

    private FeaturedPromotionStatus status;

    private Integer displayOrder;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
