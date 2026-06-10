package com.server.app.marketplace.domain.dto.response.dispute;

import com.server.app.marketplace.common.enums.DisputeResolution;
import com.server.app.marketplace.common.enums.DisputeStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisputeResponse {

    private Long id;

    private Long orderId;

    private Long orderItemId;

    private Long productId;

    private String productTitle;

    private Long buyerId;

    private String buyerName;

    private Long sellerUserId;

    private String sellerStore;

    private Integer quantity;

    private String reason;

    private DisputeStatus status;

    private String sellerResponse;

    private DisputeResolution resolution;

    private String adminNotes;

    private Long resolvedByAdminId;

    private String resolvedByAdminName;

    private Double refundAmount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
