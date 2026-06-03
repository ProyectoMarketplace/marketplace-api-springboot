package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.response.order.OrderItemResponse;
import com.server.app.marketplace.domain.dto.response.order.OrderResponse;
import com.server.app.marketplace.domain.entities.Order;
import com.server.app.marketplace.domain.entities.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderItemResponse toItemDto(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productTitle(item.getProduct().getTitle())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(item.getSubtotal())
                .build();
    }

    public OrderResponse toDto(Order order) {
        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(this::toItemDto)
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .buyerId(order.getBuyer().getId())
                .buyerName(order.getBuyer().getFullName())
                .total(order.getTotal())
                .discountAmount(order.getDiscountAmount())
                .finalTotal(order.getFinalTotal())
                .couponCode(order.getCoupon() != null ? order.getCoupon().getCode() : null)
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }
}