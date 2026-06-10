package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.CreatePriceAlertRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.PriceNotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/price-notifications")
@RequiredArgsConstructor
public class PriceNotificationController {

    private final PriceNotificationService priceNotificationService;

    @PostMapping("/alerts")
    public ResponseEntity<GeneralResponse> createPriceAlert(
            @Valid @RequestBody CreatePriceAlertRequest request
    ) {
        return buildResponse(
                "Price alert created successfully.",
                HttpStatus.CREATED,
                priceNotificationService.createPriceAlert(request)
        );
    }

    @GetMapping("/alerts/buyer/{buyerId}")
    public ResponseEntity<GeneralResponse> getAlertsByBuyer(@PathVariable Long buyerId) {
        return buildResponse(
                "Price alerts found.",
                HttpStatus.OK,
                priceNotificationService.getAlertsByBuyer(buyerId)
        );
    }

    @PatchMapping("/alerts/{id}/deactivate")
    public ResponseEntity<GeneralResponse> deactivateAlert(
            @PathVariable Long id,
            @RequestParam Long buyerId
    ) {
        return buildResponse(
                "Price alert deactivated successfully.",
                HttpStatus.OK,
                priceNotificationService.deactivateAlert(id, buyerId)
        );
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<GeneralResponse> getNotificationsByBuyer(@PathVariable Long buyerId) {
        return buildResponse(
                "Price notifications found.",
                HttpStatus.OK,
                priceNotificationService.getNotificationsByBuyer(buyerId)
        );
    }

    @GetMapping("/buyer/{buyerId}/unread")
    public ResponseEntity<GeneralResponse> getUnreadNotificationsByBuyer(@PathVariable Long buyerId) {
        return buildResponse(
                "Unread price notifications found.",
                HttpStatus.OK,
                priceNotificationService.getUnreadNotificationsByBuyer(buyerId)
        );
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<GeneralResponse> markNotificationAsRead(
            @PathVariable Long id,
            @RequestParam Long buyerId
    ) {
        return buildResponse(
                "Price notification marked as read.",
                HttpStatus.OK,
                priceNotificationService.markNotificationAsRead(id, buyerId)
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
