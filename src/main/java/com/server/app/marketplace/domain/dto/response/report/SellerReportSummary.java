package com.server.app.marketplace.domain.dto.response.report;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerReportSummary {

    private Long sellerUserId;

    private String storeName;

    private long totalProducts;

    private long approvedProducts;

    private double totalRevenue;

    private long totalUnitsSold;

    private List<ProductViewReportItem> mostViewedProducts;

    private List<ProductSalesReportItem> topSoldProducts;
}
