package com.danielmorales.validatorx.core;

import com.danielmorales.validatorx.annotations.Email;
import com.danielmorales.validatorx.annotations.NotNull;
import com.danielmorales.validatorx.annotations.Size;

import java.lang.reflect.Field;

public class ValidatorEngine {

    public ValidationResult validate(Object target) {
        ValidationResult result = new ValidationResult();
        
        if (target == null) {
            // If the entire object is null, handle it or return an error
            result.addError(new ValidationError("object", "Target object is null", null));
            return result;
        }

        Class<?> clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true); // allow access to private fields

            // Check for @NotNull
            NotNull notNull = field.getAnnotation(NotNull.class);
            if (notNull != null) {
                validateNotNull(target, field, notNull, result);
            }

            // Check for @Email
            Email email = field.getAnnotation(Email.class);
            if (email != null) {
                validateEmail(target, field, email, result);
            }

            // Check for @Size
            Size size = field.getAnnotation(Size.class);
            if (size != null) {
                validateSize(target, field, size, result);
            }
        }

        return result;
    }

    // ----- Implementation for each annotation check -----

    private void validateNotNull(Object target, Field field, NotNull annotation, ValidationResult result) {
        try {
            Object value = field.get(target);
            if (value == null) {
                String message = resolveMessage(annotation.message(), annotation.messageKey(),
                        field.getName(), "cannot be null");
                result.addError(new ValidationError(field.getName(), message, null));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void validateEmail(Object target, Field field, Email annotation, ValidationResult result) {
        try {
            Object value = field.get(target);
            if (value != null && value instanceof String) {
                String stringValue = (String) value;
                if (!isValidEmail(stringValue)) {
                    String message = resolveMessage(annotation.message(), annotation.messageKey(),
                            field.getName(), "invalid email format");
                    result.addError(new ValidationError(field.getName(), message, stringValue));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void validateSize(Object target, Field field, Size annotation, ValidationResult result) {
        try {
            Object value = field.get(target);
            if (value != null && value instanceof String) {
                String stringValue = (String) value;
                int length = stringValue.length();
                if (length < annotation.min() || length > annotation.max()) {
                    String message = resolveMessage(annotation.message(), annotation.messageKey(),
                            field.getName(),
                            String.format("length must be between %d and %d", annotation.min(), annotation.max()));
                    result.addError(new ValidationError(field.getName(), message, stringValue));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // Simple email check (placeholder)
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    // ----- Message resolution logic -----
    private String resolveMessage(String customMessage, String messageKey,
                                  String fieldName, String defaultMsg) {
        // If the developer specified a custom message, use it:
        if (!customMessage.isEmpty()) {
            return customMessage;
        }
        
        // Otherwise, if we have a localized message resolver, call it here:
        // e.g. MessageResolver.getMessage(messageKey, fieldName)
        // For now, just fallback to default or messageKey:
        
        // Example fallback: "Field 'email' invalid email format"
        return String.format("Field '%s' %s", fieldName, defaultMsg);
    }
}
