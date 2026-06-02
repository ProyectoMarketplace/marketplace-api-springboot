package com.server.app.marketplace.domain.dto.response.product;

import com.server.app.marketplace.common.enums.ProductStatus;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;

    private String title;

    private String description;

    private Double price;

    private Integer stock;

    private ProductStatus status;

    private Integer views;

    private String category;

    private String sellerStore;
}