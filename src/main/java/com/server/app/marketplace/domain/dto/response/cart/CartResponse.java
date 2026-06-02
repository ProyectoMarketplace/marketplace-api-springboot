package com.server.app.marketplace.domain.dto.response.cart;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    private Long id;

    private Long buyerId;

    private String buyerName;

    private Boolean active;

    private List<CartItemResponse> items;

    private Double total;
}