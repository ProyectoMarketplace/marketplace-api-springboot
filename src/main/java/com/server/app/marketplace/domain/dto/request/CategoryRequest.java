package com.server.app.marketplace.domain.dto.request;

import com.server.app.marketplace.common.validations.UniqueCategoryName;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Category name is required.")
    @UniqueCategoryName
    private String name;

    private String description;
}