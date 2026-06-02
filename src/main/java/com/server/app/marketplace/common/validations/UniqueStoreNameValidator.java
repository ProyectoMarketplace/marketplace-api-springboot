package com.server.app.marketplace.common.validations;

import com.server.app.marketplace.repositories.SellerProfileRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueStoreNameValidator implements ConstraintValidator<UniqueStoreName, String> {

    private final SellerProfileRepository sellerProfileRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        return !sellerProfileRepository.existsByStoreNameIgnoreCase(value);
    }
}