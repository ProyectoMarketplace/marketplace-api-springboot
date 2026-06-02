package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.AddCartItemRequest;
import com.server.app.marketplace.domain.dto.request.UpdateCartItemRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/{buyerId}/items")
    public ResponseEntity<GeneralResponse> addItem(
            @PathVariable Long buyerId,
            @Valid @RequestBody AddCartItemRequest request
    ) {
        return buildResponse(
                "Item added to cart successfully.",
                HttpStatus.CREATED,
                cartService.addItem(buyerId, request)
        );
    }

    @GetMapping("/{buyerId}")
    public ResponseEntity<GeneralResponse> getCart(@PathVariable Long buyerId) {
        return buildResponse(
                "Cart found.",
                HttpStatus.OK,
                cartService.getCart(buyerId)
        );
    }

    @PutMapping("/{buyerId}/items/{itemId}")
    public ResponseEntity<GeneralResponse> updateItem(
            @PathVariable Long buyerId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        return buildResponse(
                "Cart item updated successfully.",
                HttpStatus.OK,
                cartService.updateItem(buyerId, itemId, request)
        );
    }

    @DeleteMapping("/{buyerId}/items/{itemId}")
    public ResponseEntity<GeneralResponse> removeItem(
            @PathVariable Long buyerId,
            @PathVariable Long itemId
    ) {
        return buildResponse(
                "Cart item removed successfully.",
                HttpStatus.OK,
                cartService.removeItem(buyerId, itemId)
        );
    }

    @DeleteMapping("/{buyerId}/clear")
    public ResponseEntity<GeneralResponse> clearCart(@PathVariable Long buyerId) {
        return buildResponse(
                "Cart cleared successfully.",
                HttpStatus.OK,
                cartService.clearCart(buyerId)
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