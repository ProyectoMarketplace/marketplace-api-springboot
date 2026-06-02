package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.CategoryRequest;
import com.server.app.marketplace.domain.dto.response.category.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    CategoryResponse deleteCategory(Long id);
}