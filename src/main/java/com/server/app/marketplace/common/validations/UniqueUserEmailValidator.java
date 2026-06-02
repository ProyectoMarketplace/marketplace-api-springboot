package com.server.app.marketplace.common.validations;

import com.server.app.marketplace.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        return !userRepository.existsByEmailIgnoreCase(value);
    }
}