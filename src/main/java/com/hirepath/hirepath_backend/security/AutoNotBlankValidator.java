package com.hirepath.hirepath_backend.security;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class AutoNotBlankValidator implements ConstraintValidator<AutoNotBlank, Object> {
    @Override
    public void initialize(AutoNotBlank constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        boolean isValid = true;
        Field[] fields = value.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.getType() == String.class) {
                try {
                    field.setAccessible(true);
                    String fieldValue = (String) field.get(value);
                    if (fieldValue == null || fieldValue.trim().isEmpty()) {
                        isValid = false;
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(
                                        field.getName().substring(0, 1).toUpperCase() +
                                                field.getName().substring(1) + " must not be empty"
                                )
                                .addPropertyNode(field.getName())
                                .addConstraintViolation();
                    }
                } catch (IllegalAccessException e) {
                    isValid = false;
                }
            }
        }

        return isValid;
    }
}