package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.response.dispute.DisputeResponse;
import com.server.app.marketplace.domain.entities.Dispute;
import org.springframework.stereotype.Component;

@Component
public class DisputeMapper {

    public DisputeResponse toDto(Dispute dispute) {
        return DisputeResponse.builder()
                .id(dispute.getId())
                .orderId(dispute.getOrderItem().getOrder().getId())
                .orderItemId(dispute.getOrderItem().getId())
                .productId(dispute.getOrderItem().getProduct().getId())
                .productTitle(dispute.getOrderItem().getProduct().getTitle())
                .buyerId(dispute.getBuyer().getId())
                .buyerName(dispute.getBuyer().getFullName())
                .sellerUserId(dispute.getSeller().getId())
                .sellerStore(dispute.getOrderItem().getProduct().getSellerProfile().getStoreName())
                .quantity(dispute.getQuantity())
                .reason(dispute.getReason())
                .status(dispute.getStatus())
                .sellerResponse(dispute.getSellerResponse())
                .resolution(dispute.getResolution())
                .adminNotes(dispute.getAdminNotes())
                .resolvedByAdminId(dispute.getResolvedByAdmin() != null
                        ? dispute.getResolvedByAdmin().getId()
                        : null)
                .resolvedByAdminName(dispute.getResolvedByAdmin() != null
                        ? dispute.getResolvedByAdmin().getFullName()
                        : null)
                .refundAmount(dispute.getRefundAmount())
                .createdAt(dispute.getCreatedAt())
                .updatedAt(dispute.getUpdatedAt())
                .build();
    }
}
