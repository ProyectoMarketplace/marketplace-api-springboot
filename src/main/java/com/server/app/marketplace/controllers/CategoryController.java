package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.CategoryRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<GeneralResponse> createCategory(
            @Valid @RequestBody CategoryRequest request
    ) {
        return buildResponse(
                "Category created successfully.",
                HttpStatus.CREATED,
                categoryService.createCategory(request)
        );
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllCategories() {
        return buildResponse(
                "Categories found.",
                HttpStatus.OK,
                categoryService.getAllCategories()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getCategoryById(@PathVariable Long id) {
        return buildResponse(
                "Category found.",
                HttpStatus.OK,
                categoryService.getCategoryById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request
    ) {
        return buildResponse(
                "Category updated successfully.",
                HttpStatus.OK,
                categoryService.updateCategory(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteCategory(@PathVariable Long id) {
        return buildResponse(
                "Category disabled successfully.",
                HttpStatus.OK,
                categoryService.deleteCategory(id)
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