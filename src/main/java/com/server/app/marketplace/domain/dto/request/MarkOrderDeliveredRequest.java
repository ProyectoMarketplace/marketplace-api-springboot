package com.server.app.marketplace.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarkOrderDeliveredRequest {

    @NotNull(message = "Seller user id is required.")
    private Long sellerUserId;
}
