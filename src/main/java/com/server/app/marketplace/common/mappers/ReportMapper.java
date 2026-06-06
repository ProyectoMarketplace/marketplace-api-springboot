package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.response.report.ProductSalesReportItem;
import com.server.app.marketplace.domain.dto.response.report.ProductViewReportItem;
import com.server.app.marketplace.domain.entities.Product;
import com.server.app.marketplace.repositories.projection.ProductSalesAggregate;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public ProductViewReportItem toViewReportItem(Product product) {
        return ProductViewReportItem.builder()
                .productId(product.getId())
                .title(product.getTitle())
                .views(product.getViews())
                .price(product.getPrice())
                .status(product.getStatus())
                .sellerStore(product.getSellerProfile() != null
                        ? product.getSellerProfile().getStoreName()
                        : null)
                .category(product.getCategory() != null
                        ? product.getCategory().getName()
                        : null)
                .build();
    }

    public ProductSalesReportItem toSalesReportItem(ProductSalesAggregate aggregate) {
        return ProductSalesReportItem.builder()
                .productId(aggregate.getProductId())
                .title(aggregate.getProductTitle())
                .views(aggregate.getViews())
                .totalQuantitySold(aggregate.getTotalQuantitySold())
                .totalRevenue(aggregate.getTotalRevenue())
                .build();
    }
}
