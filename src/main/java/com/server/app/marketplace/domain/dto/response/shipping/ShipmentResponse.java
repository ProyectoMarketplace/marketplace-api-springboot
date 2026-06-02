package com.server.app.marketplace.domain.dto.response.shipping;

import com.server.app.marketplace.common.enums.ShipmentStatus;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentResponse {

    private Long id;

    private Long orderId;

    private Long shippingMethodId;

    private String shippingMethod;

    private String provider;

    private Double cost;

    private ShipmentStatus status;

    private String trackingCode;
}