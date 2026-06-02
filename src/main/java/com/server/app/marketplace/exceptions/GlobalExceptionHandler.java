package com.server.app.marketplace.exceptions;

import com.server.app.marketplace.domain.dto.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessRule(BusinessRuleException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid value for parameter: " + e.getName());
    }

    public ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, Object data) {
        String uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .build()
                .getPath();

        return ResponseEntity
                .status(status)
                .body(ApiErrorResponse.builder()
                        .uri(uri)
                        .message(data)
                        .status(status.value())
                        .time(LocalDateTime.now())
                        .build());
    }
}