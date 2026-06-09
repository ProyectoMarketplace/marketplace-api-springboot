package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.response.return_.ReturnResponse;
import com.server.app.marketplace.domain.entities.ProductReturn;
import org.springframework.stereotype.Component;

@Component
public class ReturnMapper {

    public ReturnResponse toDto(ProductReturn productReturn) {
        return ReturnResponse.builder()
                .id(productReturn.getId())
                .orderId(productReturn.getOrderItem().getOrder().getId())
                .orderItemId(productReturn.getOrderItem().getId())
                .productId(productReturn.getOrderItem().getProduct().getId())
                .productTitle(productReturn.getOrderItem().getProduct().getTitle())
                .buyerId(productReturn.getBuyer().getId())
                .buyerName(productReturn.getBuyer().getFullName())
                .sellerUserId(productReturn.getOrderItem().getProduct().getSellerProfile().getUser().getId())
                .sellerStore(productReturn.getOrderItem().getProduct().getSellerProfile().getStoreName())
                .quantity(productReturn.getQuantity())
                .reason(productReturn.getReason())
                .status(productReturn.getStatus())
                .sellerResponse(productReturn.getSellerResponse())
                .refundAmount(productReturn.getRefundAmount())
                .createdAt(productReturn.getCreatedAt())
                .updatedAt(productReturn.getUpdatedAt())
                .build();
    }
}
