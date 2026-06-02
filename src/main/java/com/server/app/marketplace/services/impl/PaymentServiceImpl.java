package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.enums.OrderStatus;
import com.server.app.marketplace.common.enums.PaymentMethod;
import com.server.app.marketplace.common.enums.PaymentStatus;
import com.server.app.marketplace.common.mappers.PaymentMapper;
import com.server.app.marketplace.domain.dto.request.SimulatePaymentRequest;
import com.server.app.marketplace.domain.dto.response.payment.PaymentResponse;
import com.server.app.marketplace.domain.entities.Order;
import com.server.app.marketplace.domain.entities.Payment;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.OrderRepository;
import com.server.app.marketplace.repositories.PaymentRepository;
import com.server.app.marketplace.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final OrderRepository orderRepository;

    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse simulatePayment(Long orderId, SimulatePaymentRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        if (paymentRepository.existsByOrderId(orderId)) {
            throw new BusinessRuleException("This order already has a payment registered.");
        }

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new BusinessRuleException("Only orders with CREATED status can be paid.");
        }

        PaymentStatus paymentStatus = request.getApproved()
                ? PaymentStatus.APPROVED
                : PaymentStatus.REJECTED;

        LocalDateTime paidAt = request.getApproved()
                ? LocalDateTime.now()
                : null;

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotal())
                .method(PaymentMethod.SIMULATED_CARD)
                .status(paymentStatus)
                .transactionReference(UUID.randomUUID().toString())
                .paidAt(paidAt)
                .build();

        if (paymentStatus == PaymentStatus.APPROVED) {
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
        }

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toDto(savedPayment);
    }

    @Override
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found."));

        return paymentMapper.toDto(payment);
    }
}