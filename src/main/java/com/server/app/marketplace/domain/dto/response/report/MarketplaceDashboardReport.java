package com.server.app.marketplace.domain.dto.response.report;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketplaceDashboardReport {

    private SalesOverviewReport salesOverview;

    private List<ProductViewReportItem> mostViewedProducts;

    private List<ProductSalesReportItem> topSoldProducts;
}
