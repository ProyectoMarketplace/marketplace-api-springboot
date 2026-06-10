package com.server.app.marketplace.domain.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductPriceRequest {

    @NotNull(message = "Seller user id is required.")
    private Long sellerUserId;

    @NotNull(message = "New price is required.")
    @DecimalMin(value = "0.01", message = "New price must be greater than zero.")
    private Double newPrice;
}
