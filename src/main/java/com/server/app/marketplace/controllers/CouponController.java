package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.CreateCouponRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<GeneralResponse> createCoupon(
            @Valid @RequestBody CreateCouponRequest request
    ) {
        return buildResponse(
                "Coupon created successfully.",
                HttpStatus.CREATED,
                couponService.createCoupon(request)
        );
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getActiveCoupons() {
        return buildResponse(
                "Coupons found.",
                HttpStatus.OK,
                couponService.getActiveCoupons()
        );
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<GeneralResponse> getCouponByCode(@PathVariable String code) {
        return buildResponse(
                "Coupon found.",
                HttpStatus.OK,
                couponService.getCouponByCode(code)
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<GeneralResponse> deactivateCoupon(@PathVariable Long id) {
        return buildResponse(
                "Coupon deactivated successfully.",
                HttpStatus.OK,
                couponService.deactivateCoupon(id)
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
