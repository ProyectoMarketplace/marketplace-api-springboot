package com.server.app.marketplace.domain.dto.request;

import com.server.app.marketplace.common.enums.DisputeResolution;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResolveDisputeRequest {

    @NotNull(message = "Admin user id is required.")
    private Long adminUserId;

    @NotNull(message = "Resolution is required.")
    private DisputeResolution resolution;

    @NotBlank(message = "Admin notes are required.")
    private String adminNotes;
}
