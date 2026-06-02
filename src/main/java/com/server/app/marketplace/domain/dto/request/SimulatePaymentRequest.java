package com.server.app.marketplace.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimulatePaymentRequest {

    @NotNull(message = "Payment approval value is required.")
    private Boolean approved;
}