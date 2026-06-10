package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.CreateProductRequest;
import com.server.app.marketplace.domain.dto.request.UpdateProductPriceRequest;
import com.server.app.marketplace.domain.dto.response.product.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(
            Long sellerUserId,
            CreateProductRequest request
    );

    List<ProductResponse> getAllApprovedProducts();

    ProductResponse getProductById(Long id);

    ProductResponse approveProduct(Long id);

    ProductResponse rejectProduct(Long id);

    ProductResponse deleteProduct(Long id);

    ProductResponse updateProductPrice(Long id, UpdateProductPriceRequest request);
}