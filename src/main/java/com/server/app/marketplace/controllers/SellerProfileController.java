package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.SellerProfileRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.SellerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/sellers/profile")
@RequiredArgsConstructor
public class SellerProfileController {

    private final SellerProfileService sellerProfileService;

    @PostMapping("/{userId}")
    public ResponseEntity<GeneralResponse> createProfile(
            @PathVariable Long userId,
            @Valid @RequestBody SellerProfileRequest request
    ) {
        return buildResponse(
                "Seller profile created successfully.",
                HttpStatus.CREATED,
                sellerProfileService.createProfile(userId, request)
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GeneralResponse> getProfileByUserId(@PathVariable Long userId) {
        return buildResponse(
                "Seller profile found.",
                HttpStatus.OK,
                sellerProfileService.getProfileByUserId(userId)
        );
    }

    @PutMapping("/{userId}")
    public ResponseEntity<GeneralResponse> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody SellerProfileRequest request
    ) {
        return buildResponse(
                "Seller profile updated successfully.",
                HttpStatus.OK,
                sellerProfileService.updateProfile(userId, request)
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