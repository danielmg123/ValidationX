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

/**
 * The {@code ValidatorEngine} class is responsible for performing annotation-based validation on objects.
 * It scans fields for validation annotations and applies the appropriate validation rules.
 *
 * <p>Validations supported:
 * <ul>
 *     <li>{@link NotNull} - Ensures a field is not null</li>
 *     <li>{@link Email} - Ensures a field contains a valid email</li>
 *     <li>{@link Size} - Ensures a string field has a length within the specified range</li>
 *     <li>{@link Min} - Ensures a numeric field meets a minimum value</li>
 *     <li>{@link Max} - Ensures a numeric field does not exceed a maximum value</li>
 *     <li>{@link Pattern} - Ensures a string field matches a given regex pattern</li>
 * </ul>
 *
 * <p>Nested objects with {@code NotNull} annotation will be validated recursively.
 */
public class ValidatorEngine {

    /**
     * Accumulates all annotation-based validations and returns a {@code ValidationResult}.
     * This method does not throw an exception if validation errors are found.
     *
     * @param target the object to validate
     * @return a {@code ValidationResult} containing any validation errors found
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

                    // Automatic cascading into nested objects
                    try {
                        Object nestedObject = field.get(target);
                        if (nestedObject != null && shouldCascade(nestedObject)) {
                            ValidationResult nestedResult = accumulateValidate(nestedObject);
                            result.getErrors().addAll(nestedResult.getErrors());
                        }
                    } catch (IllegalAccessException e) {
                        result.addError(new ValidationError(field.getName(), "Unable to access nested object", null));
                    }
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
     * Checks if a nested object should be validated recursively.
     *
     * @param obj the object to check
     * @return {@code true} if the object should be validated, {@code false} otherwise
     */
    private boolean shouldCascade(Object obj) {
        Class<?> clazz = obj.getClass();
        return !isPrimitiveOrWrapper(clazz) && !clazz.isEnum() && !(obj instanceof String);
    }

    /**
     * Determines if a given type is a primitive or wrapper class.
     *
     * @param type the class type
     * @return {@code true} if the type is a primitive or wrapper, {@code false} otherwise
     */
    private boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() ||
               type == Byte.class ||
               type == Short.class ||
               type == Integer.class ||
               type == Long.class ||
               type == Float.class ||
               type == Double.class ||
               type == Boolean.class ||
               type == Character.class ||
               type == String.class;
    }

    /**
     * Accumulates all annotation-based validations and throws a {@code ValidationException}
     * if any errors are found.
     *
     * @param target the object to validate
     * @return a {@code ValidationResult} if no validation errors are found
     * @throws ValidationException if any validation errors are detected
     */
    public ValidationResult validateAndThrow(Object target) {
        ValidationResult result = accumulateValidate(target);
        if (result.hasErrors()) {
            throw new ValidationException("Validation failed", result);
        }
        return result;
    }

    // ----- Implementation for each annotation check -----

    /**
     * Validates that a field is not null.
     *
     * @param target     the object being validated
     * @param field      the field to check
     * @param annotation the {@code NotNull} annotation instance
     * @param result     the validation result to accumulate errors
     */
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

    /**
     * Validates that a field contains a valid email address.
     *
     * @param target     the object being validated
     * @param field      the field to check
     * @param annotation the {@code Email} annotation instance
     * @param result     the validation result to accumulate errors
     */
    private void validateEmail(Object target, Field field, Email annotation, ValidationResult result) {
        try {
            Object value = field.get(target);
            if (value instanceof String) {
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

    /**
     * Validates that a field's length is within the specified range.
     *
     * @param target     the object being validated
     * @param field      the field to check
     * @param annotation the {@code Size} annotation instance
     * @param result     the validation result to accumulate errors
     */
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

    /**
     * Validates that a numeric field meets the minimum value constraint.
     *
     * @param target     the object being validated
     * @param field      the field to check
     * @param annotation the {@code Min} annotation instance
     * @param result     the validation result to accumulate errors
     */
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


    /**
     * Validates that a numeric field does not exceed the maximum value constraint.
     *
     * @param target     the object being validated
     * @param field      the field to check
     * @param annotation the {@code Max} annotation instance
     * @param result     the validation result to accumulate errors
     */
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

    /**
     * Validates that a string field matches a given regex pattern.
     *
     * @param target     the object being validated
     * @param field      the field to check
     * @param annotation the {@code Pattern} annotation instance
     * @param result     the validation result to accumulate errors
     */
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

    /**
     * Checks whether a given email string is valid.
     *
     * @param email the email address to check
     * @return {@code true} if the email is valid, {@code false} otherwise
     */
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    // ----- Message resolution logic -----

    /**
     * Resolves a validation message, either from a custom message, a localized message key, or a default message.
     *
     * @param customMessage the custom message (if provided)
     * @param messageKey    the message key for localized messages
     * @param fieldName     the name of the field being validated
     * @param defaultMsg    the default message to use if neither customMessage nor messageKey is available
     * @return the resolved validation message
     */
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