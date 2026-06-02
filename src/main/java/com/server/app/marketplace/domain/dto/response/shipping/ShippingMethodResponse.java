package com.server.app.marketplace.domain.dto.response.shipping;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingMethodResponse {

    private Long id;

    private String name;

    private String provider;

    private Double baseCost;

    private Boolean active;
}