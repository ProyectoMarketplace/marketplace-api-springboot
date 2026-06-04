package com.server.app.marketplace.domain.dto.response.report;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesOverviewReport {

    private LocalDateTime periodFrom;

    private LocalDateTime periodTo;

    private long totalOrders;

    private long paidOrders;

    private long shippedOrders;

    private long deliveredOrders;

    private long cancelledOrders;

    private double totalRevenue;

    private Map<String, Long> usersByRole;

    private Map<String, Long> productsByStatus;
}
