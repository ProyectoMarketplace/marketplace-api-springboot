package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.LoginRequest;
import com.server.app.marketplace.domain.dto.request.RegisterRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return buildResponse(
                "User registered successfully.",
                HttpStatus.CREATED,
                authService.register(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<GeneralResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return buildResponse(
                "User logged in successfully.",
                HttpStatus.OK,
                authService.login(request)
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