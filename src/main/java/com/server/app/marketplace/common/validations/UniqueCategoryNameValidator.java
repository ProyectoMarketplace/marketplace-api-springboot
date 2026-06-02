package com.server.app.marketplace.common.validations;

import com.server.app.marketplace.repositories.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueCategoryNameValidator implements ConstraintValidator<UniqueCategoryName, String> {

    private final CategoryRepository categoryRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        return !categoryRepository.existsByNameIgnoreCase(value);
    }
}