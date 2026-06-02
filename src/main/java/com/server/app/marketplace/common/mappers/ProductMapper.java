package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.common.enums.ProductStatus;
import com.server.app.marketplace.domain.dto.request.CreateProductRequest;
import com.server.app.marketplace.domain.dto.response.product.ProductResponse;
import com.server.app.marketplace.domain.entities.Category;
import com.server.app.marketplace.domain.entities.Product;
import com.server.app.marketplace.domain.entities.SellerProfile;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(
            CreateProductRequest request,
            SellerProfile seller,
            Category category
    ) {

        return Product.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .status(ProductStatus.PENDING)
                .prohibited(false)
                .views(0)
                .sellerProfile(seller)
                .category(category)
                .build();
    }

    public ProductResponse toDto(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus())
                .views(product.getViews())
                .category(product.getCategory().getName())
                .sellerStore(product.getSellerProfile().getStoreName())
                .build();
    }
}