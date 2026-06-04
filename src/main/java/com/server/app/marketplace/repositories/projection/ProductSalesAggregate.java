package com.server.app.marketplace.repositories.projection;

public interface ProductSalesAggregate {

    Long getProductId();

    String getProductTitle();

    Integer getViews();

    Long getTotalQuantitySold();

    Double getTotalRevenue();
}
