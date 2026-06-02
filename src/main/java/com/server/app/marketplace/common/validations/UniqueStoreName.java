package com.server.app.marketplace.common.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueStoreNameValidator.class)
@Documented
public @interface UniqueStoreName {

    String message() default "Store name is already registered.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}