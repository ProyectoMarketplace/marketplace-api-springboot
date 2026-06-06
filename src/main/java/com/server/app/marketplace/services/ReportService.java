package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.response.report.*;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    MarketplaceDashboardReport getAdminDashboard(Long requestingUserId, LocalDate from, LocalDate to, int limit);

    SalesOverviewReport getSalesOverview(Long requestingUserId, LocalDate from, LocalDate to);

    List<ProductViewReportItem> getMostViewedProducts(Long requestingUserId, Long sellerUserId, int limit);

    List<ProductSalesReportItem> getTopSoldProducts(Long requestingUserId, Long sellerUserId, int limit);

    SellerReportSummary getSellerSummary(Long requestingUserId, Long sellerUserId, int limit);
}
