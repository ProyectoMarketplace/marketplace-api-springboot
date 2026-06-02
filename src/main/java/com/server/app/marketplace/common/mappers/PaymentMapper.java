package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.response.payment.PaymentResponse;
import com.server.app.marketplace.domain.entities.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponse toDto(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .transactionReference(payment.getTransactionReference())
                .paidAt(payment.getPaidAt())
                .orderStatus(payment.getOrder().getStatus())
                .build();
    }
}