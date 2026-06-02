package com.server.app.marketplace.domain.dto.response.cart;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {

    private Long id;

    private Long productId;

    private String productTitle;

    private Integer quantity;

    private Double unitPrice;

    private Double subtotal;
}