package com.server.app.marketplace.domain.dto.response.order;

import com.server.app.marketplace.common.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;

    private Long buyerId;

    private String buyerName;

    private Double total;

    private OrderStatus status;

    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;
}