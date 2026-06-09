package com.server.app.marketplace.domain.dto.response.return_;

import com.server.app.marketplace.common.enums.ReturnStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnResponse {

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

    private ReturnStatus status;

    private String sellerResponse;

    private Double refundAmount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
