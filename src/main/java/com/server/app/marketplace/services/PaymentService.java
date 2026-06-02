package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.SimulatePaymentRequest;
import com.server.app.marketplace.domain.dto.response.payment.PaymentResponse;

public interface PaymentService {

    PaymentResponse simulatePayment(Long orderId, SimulatePaymentRequest request);

    PaymentResponse getPaymentByOrderId(Long orderId);
}