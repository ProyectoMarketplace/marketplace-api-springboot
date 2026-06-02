package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.SimulatePaymentRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/simulate/{orderId}")
    public ResponseEntity<GeneralResponse> simulatePayment(
            @PathVariable Long orderId,
            @Valid @RequestBody SimulatePaymentRequest request
    ) {
        return buildResponse(
                "Payment simulation completed.",
                HttpStatus.CREATED,
                paymentService.simulatePayment(orderId, request)
        );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<GeneralResponse> getPaymentByOrderId(@PathVariable Long orderId) {
        return buildResponse(
                "Payment found.",
                HttpStatus.OK,
                paymentService.getPaymentByOrderId(orderId)
        );
    }

    public ResponseEntity<GeneralResponse> buildResponse(
            String message,
            HttpStatus status,
            Object data
    ) {
        String uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .build()
                .getPath();

        return ResponseEntity
                .status(status)
                .body(GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(LocalDateTime.now())
                        .data(data)
                        .build());
    }
}