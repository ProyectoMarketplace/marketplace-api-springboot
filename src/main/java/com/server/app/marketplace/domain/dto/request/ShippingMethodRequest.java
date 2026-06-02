package com.server.app.marketplace.domain.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingMethodRequest {

    @NotBlank(message = "Shipping method name is required.")
    private String name;

    @NotBlank(message = "Provider is required.")
    private String provider;

    @NotNull(message = "Base cost is required.")
    @DecimalMin(value = "0.01", message = "Base cost must be greater than zero.")
    private Double baseCost;
}