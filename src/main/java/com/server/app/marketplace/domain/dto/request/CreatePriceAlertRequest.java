package com.server.app.marketplace.domain.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePriceAlertRequest {

    @NotNull(message = "Buyer id is required.")
    private Long buyerId;

    @NotNull(message = "Product id is required.")
    private Long productId;

    @DecimalMin(value = "0.01", message = "Target price must be greater than zero.")
    private Double targetPrice;
}
