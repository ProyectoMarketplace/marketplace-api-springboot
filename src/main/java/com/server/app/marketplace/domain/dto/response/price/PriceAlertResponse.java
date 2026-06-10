package com.server.app.marketplace.domain.dto.response.price;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceAlertResponse {

    private Long id;

    private Long buyerId;

    private Long productId;

    private String productTitle;

    private Double currentPrice;

    private Double targetPrice;

    private Boolean active;

    private LocalDateTime createdAt;
}
