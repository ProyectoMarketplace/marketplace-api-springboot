package com.server.app.marketplace.common.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUserEmailValidator.class)
@Documented
public @interface UniqueUserEmail {

    String message() default "Email is already registered.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}