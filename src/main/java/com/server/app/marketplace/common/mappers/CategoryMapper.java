package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.request.CategoryRequest;
import com.server.app.marketplace.domain.dto.response.category.CategoryResponse;
import com.server.app.marketplace.domain.entities.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {
        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .active(true)
                .build();
    }

    public void updateEntity(Category category, CategoryRequest request) {
        category.setName(request.getName());
        category.setDescription(request.getDescription());
    }

    public CategoryResponse toDto(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .build();
    }
}