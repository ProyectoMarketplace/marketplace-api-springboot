package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout/{buyerId}")
    public ResponseEntity<GeneralResponse> checkout(@PathVariable Long buyerId) {
        return buildResponse(
                "Order created successfully.",
                HttpStatus.CREATED,
                orderService.checkout(buyerId)
        );
    }

    @GetMapping("/my-orders/{buyerId}")
    public ResponseEntity<GeneralResponse> getMyOrders(@PathVariable Long buyerId) {
        return buildResponse(
                "Orders found.",
                HttpStatus.OK,
                orderService.getMyOrders(buyerId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getOrderById(@PathVariable Long id) {
        return buildResponse(
                "Order found.",
                HttpStatus.OK,
                orderService.getOrderById(id)
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