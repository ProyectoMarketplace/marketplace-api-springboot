package com.server.app.marketplace.domain.dto.request;

import com.server.app.marketplace.common.validations.UniqueStoreName;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerProfileRequest {

    @NotBlank(message = "Store name is required.")
    @UniqueStoreName
    private String storeName;
}