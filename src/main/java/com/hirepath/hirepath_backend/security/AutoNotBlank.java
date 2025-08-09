package com.hirepath.hirepath_backend.security;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AutoNotBlankValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoNotBlank {
    String message() default "{field} must not be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
