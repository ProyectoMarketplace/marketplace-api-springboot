package com.server.app.marketplace.domain.dto.response.price;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceNotificationResponse {

    private Long id;

    private Long buyerId;

    private Long productId;

    private String productTitle;

    private Double previousPrice;

    private Double newPrice;

    private String message;

    private Boolean read;

    private LocalDateTime createdAt;
}
