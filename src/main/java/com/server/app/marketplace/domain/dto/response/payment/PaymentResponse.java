package com.server.app.marketplace.domain.dto.response.payment;

import com.server.app.marketplace.common.enums.OrderStatus;
import com.server.app.marketplace.common.enums.PaymentMethod;
import com.server.app.marketplace.common.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private Long id;

    private Long orderId;

    private Double amount;

    private PaymentMethod method;

    private PaymentStatus status;

    private String transactionReference;

    private LocalDateTime paidAt;

    private OrderStatus orderStatus;
}