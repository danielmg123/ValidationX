package com.danielmorales.validatorx.core;

import com.danielmorales.validatorx.annotations.Email;
import com.danielmorales.validatorx.annotations.Max;
import com.danielmorales.validatorx.annotations.Min;
import com.danielmorales.validatorx.annotations.NotNull;
import com.danielmorales.validatorx.annotations.Pattern;
import com.danielmorales.validatorx.annotations.Size;
import com.danielmorales.validatorx.i18n.MessageResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class ValidatorEngine {

    /**
     * Accumulates all annotation-based validations and returns a ValidationResult.
     * This method does not throw an exception if validation errors are found.
     *
     * @param target the object to validate
     * @return a ValidationResult containing any errors found
     */
    public ValidationResult accumulateValidate(Object target) {
        ValidationResult result = new ValidationResult();

        if (target == null) {
            result.addError(new ValidationError("object", "Target object is null", null));
            return result;
        }

        Class<?> clazz = target.getClass();
        List<ReflectionCache.FieldAnnotations> fieldAnnotationsList = ReflectionCache.getFieldAnnotations(clazz);

        for (ReflectionCache.FieldAnnotations fa : fieldAnnotationsList) {
            Field field = fa.getField();
            for (Annotation annotation : fa.getAnnotations()) {
                if (annotation instanceof NotNull) {
                    validateNotNull(target, field, (NotNull) annotation, result);
                } else if (annotation instanceof Email) {
                    validateEmail(target, field, (Email) annotation, result);
                } else if (annotation instanceof Size) {
                    validateSize(target, field, (Size) annotation, result);
                } else if (annotation instanceof Min) {
                    validateMin(target, field, (Min) annotation, result);
                } else if (annotation instanceof Max) {
                    validateMax(target, field, (Max) annotation, result);
                } else if (annotation instanceof Pattern) {
                    validatePattern(target, field, (Pattern) annotation, result);
                }
            }
        }

        return result;
    }

    /**
     * Accumulates all annotation-based validations and throws a ValidationException
     * if any errors are found. Otherwise, returns the ValidationResult.
     *
     * @param target the object to validate
     * @return a ValidationResult with no errors if validation passes
     * @throws ValidationException if any validation errors are found
     */
    public ValidationResult validateAndThrow(Object target) {
        ValidationResult result = accumulateValidate(target);
        if (result.hasErrors()) {
            throw new ValidationException("Validation failed", result);
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

    private void validateMin(Object target, Field field, Min annotation, ValidationResult result) {
        try {
            Object value = field.get(target);
            if (value instanceof Number) {
                Number number = (Number) value;
                if (number.longValue() < annotation.value()) {
                    String message = resolveMessage(
                        annotation.message(),
                        annotation.messageKey(),
                        field.getName(),
                        String.format("must be >= %d", annotation.value())
                    );
                    result.addError(new ValidationError(field.getName(), message, number));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void validateMax(Object target, Field field, Max annotation, ValidationResult result) {
        try {
            Object value = field.get(target);
            if (value instanceof Number) {
                Number number = (Number) value;
                if (number.longValue() > annotation.value()) {
                    String message = resolveMessage(
                        annotation.message(),
                        annotation.messageKey(),
                        field.getName(),
                        String.format("must be <= %d", annotation.value())
                    );
                    result.addError(new ValidationError(field.getName(), message, number));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void validatePattern(Object target, Field field, Pattern annotation, ValidationResult result) {
        try {
            Object value = field.get(target);
            if (value instanceof String) {
                String stringValue = (String) value;
                if (!stringValue.matches(annotation.regex())) {
                    String message = resolveMessage(
                        annotation.message(),
                        annotation.messageKey(),
                        field.getName(),
                        String.format("must match regex '%s'", annotation.regex())
                    );
                    result.addError(new ValidationError(field.getName(), message, stringValue));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // Simple email check (placeholder implementation)
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    // ----- Message resolution logic -----

    private String resolveMessage(String customMessage, String messageKey, String fieldName, String defaultMsg) {
        if (!customMessage.isEmpty()) {
            return customMessage;
        }

        try {
            String localized = MessageResolver.getMessage(messageKey, fieldName);
            return localized;
        } catch (Exception e) {
            return String.format("Field '%s' %s", fieldName, defaultMsg);
        }
    }
}