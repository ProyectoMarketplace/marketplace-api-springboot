package com.server.app.marketplace.domain.dto.response.seller;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerProfileResponse {

    private Long id;

    private String storeName;

    private Boolean identityVerified;

    private Double commissionRate;

    private Long userId;

    private String sellerName;

    private String email;
}