package com.server.app.marketplace.domain.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

    @NotBlank(message = "Title is required.")
    private String title;

    private String description;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero.")
    private Double price;

    @NotNull(message = "Stock is required.")
    @Min(value = 0, message = "Stock cannot be negative.")
    private Integer stock;

    @NotNull(message = "Category id is required.")
    private Long categoryId;
}