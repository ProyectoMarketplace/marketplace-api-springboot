package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.CreateDisputeRequest;
import com.server.app.marketplace.domain.dto.request.ResolveDisputeRequest;
import com.server.app.marketplace.domain.dto.request.RespondDisputeRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.DisputeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/disputes")
@RequiredArgsConstructor
public class DisputeController {

    private final DisputeService disputeService;

    @PostMapping
    public ResponseEntity<GeneralResponse> createDispute(
            @Valid @RequestBody CreateDisputeRequest request
    ) {
        return buildResponse(
                "Dispute opened successfully.",
                HttpStatus.CREATED,
                disputeService.createDispute(request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getDisputeById(@PathVariable Long id) {
        return buildResponse(
                "Dispute found.",
                HttpStatus.OK,
                disputeService.getDisputeById(id)
        );
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<GeneralResponse> getDisputesByBuyer(@PathVariable Long buyerId) {
        return buildResponse(
                "Buyer disputes found.",
                HttpStatus.OK,
                disputeService.getDisputesByBuyer(buyerId)
        );
    }

    @GetMapping("/seller/{sellerUserId}")
    public ResponseEntity<GeneralResponse> getDisputesBySeller(@PathVariable Long sellerUserId) {
        return buildResponse(
                "Seller disputes found.",
                HttpStatus.OK,
                disputeService.getDisputesBySeller(sellerUserId)
        );
    }

    @GetMapping("/admin/pending")
    public ResponseEntity<GeneralResponse> getPendingDisputesForAdmin(
            @RequestParam Long adminUserId
    ) {
        return buildResponse(
                "Pending disputes found.",
                HttpStatus.OK,
                disputeService.getPendingDisputesForAdmin(adminUserId)
        );
    }

    @PatchMapping("/{id}/respond")
    public ResponseEntity<GeneralResponse> respondToDispute(
            @PathVariable Long id,
            @Valid @RequestBody RespondDisputeRequest request
    ) {
        return buildResponse(
                "Dispute response recorded successfully.",
                HttpStatus.OK,
                disputeService.respondToDispute(id, request)
        );
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<GeneralResponse> resolveDispute(
            @PathVariable Long id,
            @Valid @RequestBody ResolveDisputeRequest request
    ) {
        return buildResponse(
                "Dispute resolved successfully.",
                HttpStatus.OK,
                disputeService.resolveDispute(id, request)
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
