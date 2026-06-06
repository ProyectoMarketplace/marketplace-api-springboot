package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/dashboard")
    public ResponseEntity<GeneralResponse> getDashboard(
            @RequestParam Long requestingUserId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return buildResponse(
                "Marketplace dashboard report generated.",
                HttpStatus.OK,
                reportService.getAdminDashboard(requestingUserId, from, to, limit)
        );
    }

    @GetMapping("/sales-overview")
    public ResponseEntity<GeneralResponse> getSalesOverview(
            @RequestParam Long requestingUserId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return buildResponse(
                "Sales overview report generated.",
                HttpStatus.OK,
                reportService.getSalesOverview(requestingUserId, from, to)
        );
    }

    @GetMapping("/products/most-viewed")
    public ResponseEntity<GeneralResponse> getMostViewedProducts(
            @RequestParam Long requestingUserId,
            @RequestParam(required = false) Long sellerUserId,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return buildResponse(
                "Most viewed products report generated.",
                HttpStatus.OK,
                reportService.getMostViewedProducts(requestingUserId, sellerUserId, limit)
        );
    }

    @GetMapping("/products/top-sold")
    public ResponseEntity<GeneralResponse> getTopSoldProducts(
            @RequestParam Long requestingUserId,
            @RequestParam(required = false) Long sellerUserId,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return buildResponse(
                "Top sold products report generated.",
                HttpStatus.OK,
                reportService.getTopSoldProducts(requestingUserId, sellerUserId, limit)
        );
    }

    @GetMapping("/sellers/{sellerUserId}")
    public ResponseEntity<GeneralResponse> getSellerSummary(
            @PathVariable Long sellerUserId,
            @RequestParam Long requestingUserId,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return buildResponse(
                "Seller report generated.",
                HttpStatus.OK,
                reportService.getSellerSummary(requestingUserId, sellerUserId, limit)
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
