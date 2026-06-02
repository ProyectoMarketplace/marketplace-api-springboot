package com.server.app.marketplace.domain.dto.response.shipping;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentCostResponse {

    private Long orderId;

    private Long shippingMethodId;

    private String shippingMethod;

    private String provider;

    private Double orderTotal;

    private Double shippingCost;

    private Double finalTotal;
}