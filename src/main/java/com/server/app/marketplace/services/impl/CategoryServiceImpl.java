package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.mappers.CategoryMapper;
import com.server.app.marketplace.domain.dto.request.CategoryRequest;
import com.server.app.marketplace.domain.dto.response.category.CategoryResponse;
import com.server.app.marketplace.domain.entities.Category;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.CategoryRepository;
import com.server.app.marketplace.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BusinessRuleException("Category name is already registered.");
        }

        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = findCategory(id);

        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = findCategory(id);

        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id)) {
            throw new BusinessRuleException("Category name is already registered.");
        }

        categoryMapper.updateEntity(category, request);
        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    @Transactional
    public CategoryResponse deleteCategory(Long id) {
        Category category = findCategory(id);

        category.setActive(false);
        Category deletedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(deletedCategory);
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
    }
}