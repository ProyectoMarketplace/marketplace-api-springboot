package com.server.app.marketplace.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateShipmentRequest {

    @NotNull(message = "Order id is required.")
    private Long orderId;

    @NotNull(message = "Shipping method id is required.")
    private Long shippingMethodId;
}