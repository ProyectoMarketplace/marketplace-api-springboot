package com.server.app.marketplace.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateConversationRequest {

    @NotNull(message = "Buyer id is required.")
    private Long buyerId;

    @NotNull(message = "Product id is required.")
    private Long productId;
}