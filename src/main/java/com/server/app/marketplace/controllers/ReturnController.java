package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.CreateReturnRequest;
import com.server.app.marketplace.domain.dto.request.ProcessReturnRefundRequest;
import com.server.app.marketplace.domain.dto.request.RejectReturnRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.ReturnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/returns")
@RequiredArgsConstructor
public class ReturnController {

    private final ReturnService returnService;

    @PostMapping
    public ResponseEntity<GeneralResponse> createReturn(
            @Valid @RequestBody CreateReturnRequest request
    ) {
        return buildResponse(
                "Return request created successfully.",
                HttpStatus.CREATED,
                returnService.createReturn(request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getReturnById(@PathVariable Long id) {
        return buildResponse(
                "Return found.",
                HttpStatus.OK,
                returnService.getReturnById(id)
        );
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<GeneralResponse> getReturnsByBuyer(@PathVariable Long buyerId) {
        return buildResponse(
                "Buyer returns found.",
                HttpStatus.OK,
                returnService.getReturnsByBuyer(buyerId)
        );
    }

    @GetMapping("/seller/{sellerUserId}")
    public ResponseEntity<GeneralResponse> getReturnsBySeller(@PathVariable Long sellerUserId) {
        return buildResponse(
                "Seller returns found.",
                HttpStatus.OK,
                returnService.getReturnsBySeller(sellerUserId)
        );
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<GeneralResponse> approveReturn(
            @PathVariable Long id,
            @RequestParam Long sellerUserId
    ) {
        return buildResponse(
                "Return approved successfully.",
                HttpStatus.OK,
                returnService.approveReturn(id, sellerUserId)
        );
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<GeneralResponse> rejectReturn(
            @PathVariable Long id,
            @Valid @RequestBody RejectReturnRequest request
    ) {
        return buildResponse(
                "Return rejected successfully.",
                HttpStatus.OK,
                returnService.rejectReturn(id, request)
        );
    }

    @PatchMapping("/{id}/refund")
    public ResponseEntity<GeneralResponse> processRefund(
            @PathVariable Long id,
            @Valid @RequestBody ProcessReturnRefundRequest request
    ) {
        return buildResponse(
                "Return refunded successfully.",
                HttpStatus.OK,
                returnService.processRefund(id, request)
        );
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<GeneralResponse> cancelReturn(
            @PathVariable Long id,
            @RequestParam Long buyerId
    ) {
        return buildResponse(
                "Return cancelled successfully.",
                HttpStatus.OK,
                returnService.cancelReturn(id, buyerId)
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
