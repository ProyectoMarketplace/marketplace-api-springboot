package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.CalculateShipmentRequest;
import com.server.app.marketplace.domain.dto.request.CreateShipmentRequest;
import com.server.app.marketplace.domain.dto.request.MarkOrderDeliveredRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping("/calculate")
    public ResponseEntity<GeneralResponse> calculateShipment(
            @Valid @RequestBody CalculateShipmentRequest request
    ) {
        return buildResponse(
                "Shipment cost calculated successfully.",
                HttpStatus.OK,
                shipmentService.calculateShipment(request)
        );
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> createShipment(
            @Valid @RequestBody CreateShipmentRequest request
    ) {
        return buildResponse(
                "Shipment created successfully.",
                HttpStatus.CREATED,
                shipmentService.createShipment(request)
        );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<GeneralResponse> getShipmentByOrderId(@PathVariable Long orderId) {
        return buildResponse(
                "Shipment found.",
                HttpStatus.OK,
                shipmentService.getShipmentByOrderId(orderId)
        );
    }

    @PatchMapping("/order/{orderId}/deliver")
    public ResponseEntity<GeneralResponse> markOrderAsDelivered(
            @PathVariable Long orderId,
            @Valid @RequestBody MarkOrderDeliveredRequest request
    ) {
        return buildResponse(
                "Order marked as delivered successfully.",
                HttpStatus.OK,
                shipmentService.markOrderAsDelivered(orderId, request)
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