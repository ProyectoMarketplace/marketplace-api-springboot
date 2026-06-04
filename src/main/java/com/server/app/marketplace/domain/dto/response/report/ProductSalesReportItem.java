package com.server.app.marketplace.domain.dto.response.report;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSalesReportItem {

    private Long productId;

    private String title;

    private Integer views;

    private Long totalQuantitySold;

    private Double totalRevenue;
}
