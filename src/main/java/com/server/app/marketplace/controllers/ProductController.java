package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.CreateProductRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/{sellerUserId}")
    public ResponseEntity<GeneralResponse> createProduct(
            @PathVariable Long sellerUserId,
            @Valid @RequestBody CreateProductRequest request
    ) {
        return buildResponse(
                "Product created successfully.",
                HttpStatus.CREATED,
                productService.createProduct(sellerUserId, request)
        );
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllApprovedProducts() {
        return buildResponse(
                "Products found.",
                HttpStatus.OK,
                productService.getAllApprovedProducts()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getProductById(@PathVariable Long id) {
        return buildResponse(
                "Product found.",
                HttpStatus.OK,
                productService.getProductById(id)
        );
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<GeneralResponse> approveProduct(@PathVariable Long id) {
        return buildResponse(
                "Product approved successfully.",
                HttpStatus.OK,
                productService.approveProduct(id)
        );
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<GeneralResponse> rejectProduct(@PathVariable Long id) {
        return buildResponse(
                "Product rejected successfully.",
                HttpStatus.OK,
                productService.rejectProduct(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteProduct(@PathVariable Long id) {
        return buildResponse(
                "Product deleted successfully.",
                HttpStatus.OK,
                productService.deleteProduct(id)
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