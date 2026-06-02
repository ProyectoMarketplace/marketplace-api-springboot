package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.ShippingMethodRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.ShippingMethodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/shipping-methods")
@RequiredArgsConstructor
public class ShippingMethodController {

    private final ShippingMethodService shippingMethodService;

    @PostMapping
    public ResponseEntity<GeneralResponse> createShippingMethod(
            @Valid @RequestBody ShippingMethodRequest request
    ) {
        return buildResponse(
                "Shipping method created successfully.",
                HttpStatus.CREATED,
                shippingMethodService.createShippingMethod(request)
        );
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getActiveShippingMethods() {
        return buildResponse(
                "Shipping methods found.",
                HttpStatus.OK,
                shippingMethodService.getActiveShippingMethods()
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