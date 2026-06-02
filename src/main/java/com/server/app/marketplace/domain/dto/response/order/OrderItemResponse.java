package com.server.app.marketplace.domain.dto.response.order;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {

    private Long id;

    private Long productId;

    private String productTitle;

    private Integer quantity;

    private Double unitPrice;

    private Double subtotal;
}