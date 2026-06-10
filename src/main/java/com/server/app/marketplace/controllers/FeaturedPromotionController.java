package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.CancelFeaturedPromotionRequest;
import com.server.app.marketplace.domain.dto.request.CreateFeaturedPromotionRequest;
import com.server.app.marketplace.domain.dto.request.PayFeaturedPromotionRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.FeaturedPromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/featured-promotions")
@RequiredArgsConstructor
public class FeaturedPromotionController {

    private final FeaturedPromotionService featuredPromotionService;

    @PostMapping
    public ResponseEntity<GeneralResponse> createPromotion(
            @Valid @RequestBody CreateFeaturedPromotionRequest request
    ) {
        return buildResponse(
                "Featured promotion created successfully.",
                HttpStatus.CREATED,
                featuredPromotionService.createPromotion(request)
        );
    }

    @GetMapping("/active")
    public ResponseEntity<GeneralResponse> getActiveFeaturedPromotions() {
        return buildResponse(
                "Active featured promotions found.",
                HttpStatus.OK,
                featuredPromotionService.getActiveFeaturedPromotions()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getPromotionById(@PathVariable Long id) {
        return buildResponse(
                "Featured promotion found.",
                HttpStatus.OK,
                featuredPromotionService.getPromotionById(id)
        );
    }

    @GetMapping("/seller/{sellerUserId}")
    public ResponseEntity<GeneralResponse> getPromotionsBySeller(@PathVariable Long sellerUserId) {
        return buildResponse(
                "Seller featured promotions found.",
                HttpStatus.OK,
                featuredPromotionService.getPromotionsBySeller(sellerUserId)
        );
    }

    @GetMapping("/admin/all")
    public ResponseEntity<GeneralResponse> getAllPromotionsForAdmin(
            @RequestParam Long adminUserId
    ) {
        return buildResponse(
                "Featured promotions found.",
                HttpStatus.OK,
                featuredPromotionService.getAllPromotionsForAdmin(adminUserId)
        );
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<GeneralResponse> payForPromotion(
            @PathVariable Long id,
            @Valid @RequestBody PayFeaturedPromotionRequest request
    ) {
        return buildResponse(
                "Featured promotion payment processed.",
                HttpStatus.OK,
                featuredPromotionService.payForPromotion(id, request)
        );
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<GeneralResponse> cancelPromotion(
            @PathVariable Long id,
            @Valid @RequestBody CancelFeaturedPromotionRequest request
    ) {
        return buildResponse(
                "Featured promotion cancelled successfully.",
                HttpStatus.OK,
                featuredPromotionService.cancelPromotion(id, request)
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
