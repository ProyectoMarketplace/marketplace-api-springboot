package com.server.app.marketplace.domain.dto.response.report;

import com.server.app.marketplace.common.enums.ProductStatus;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductViewReportItem {

    private Long productId;

    private String title;

    private Integer views;

    private Double price;

    private ProductStatus status;

    private String sellerStore;

    private String category;
}
