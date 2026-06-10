package com.server.app.marketplace.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelFeaturedPromotionRequest {

    @NotNull(message = "Requesting user id is required.")
    private Long requestingUserId;
}
