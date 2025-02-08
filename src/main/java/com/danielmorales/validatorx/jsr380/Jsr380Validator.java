package com.danielmorales.validatorx.jsr380;

import com.danielmorales.validatorx.core.ReflectionCache;
import com.danielmorales.validatorx.core.ReflectionCache.FieldAnnotations;
import com.danielmorales.validatorx.core.ValidationError;
import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.i18n.MessageResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class Jsr380Validator {

    public static ValidationResult validate(Object target) {
        ValidationResult result = new ValidationResult();
        if (target == null) {
            result.addError(new ValidationError("object", "Target object is null", null));
            return result;
        }
        List<FieldAnnotations> fieldAnnotations = ReflectionCache.getFieldAnnotations(target.getClass());
        for (FieldAnnotations fa : fieldAnnotations) {
            Field field = fa.getField();
            for (Annotation annotation : fa.getAnnotations()) {
                if (annotation instanceof javax.validation.constraints.NotNull) {
                    validateNotNull(target, field, (javax.validation.constraints.NotNull) annotation, result);
                } else if (annotation instanceof javax.validation.constraints.Size) {
                    validateSize(target, field, (javax.validation.constraints.Size) annotation, result);
                } else if (annotation instanceof javax.validation.constraints.Min) {
                    validateMin(target, field, (javax.validation.constraints.Min) annotation, result);
                } else if (annotation instanceof javax.validation.constraints.Max) {
                    validateMax(target, field, (javax.validation.constraints.Max) annotation, result);
                } else if (annotation instanceof javax.validation.constraints.Email) {
                    validateEmail(target, field, (javax.validation.constraints.Email) annotation, result);
                } else if (annotation instanceof javax.validation.constraints.Pattern) {
                    validatePattern(target, field, (javax.validation.constraints.Pattern) annotation, result);
                }
                // Add additional mappings for other JSR 380 annotations if needed.
            }
        }
        return result;
    }

    private static void validateNotNull(Object target, Field field, javax.validation.constraints.NotNull annotation, ValidationResult result) {
        try {
            Object value = field.get(target);
            if (value == null) {
                String message = !annotation.message().isEmpty()
                        ? annotation.message()
                        : MessageResolver.getMessage("error.notNull", field.getName());
                result.addError(new ValidationError(field.getName(), message, null));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void validateSize(Object target, Field field, javax.validation.constraints.Size annotation, ValidationResult result) {
        try {
            Object value = field.get(target);
            if (value instanceof String) {
                String str = (String) value;
                if (str.length() < annotation.min() || str.length() > annotation.max()) {
                    String message = !annotation.message().isEmpty()
                            ? annotation.message()
                            : MessageResolver.getMessage("error.size", field.getName());
                    result.addError(new ValidationError(field.getName(), message, str));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void validateMin(Object target, Field field, javax.validation.constraints.Min annotation, ValidationResult result) {
        try {
            Object value = field.get(target);
            if (value instanceof Number) {
                Number number = (Number) value;
                if (number.longValue() < annotation.value()) {
                    String message = !annotation.message().isEmpty()
                            ? annotation.message()
                            : MessageResolver.getMessage("error.min", field.getName(), annotation.value());
                    result.addError(new ValidationError(field.getName(), message, number));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void validateMax(Object target, Field field, javax.validation.constraints.Max annotation, ValidationResult result) {
        try {
            Object value = field.get(target);
            if (value instanceof Number) {
                Number number = (Number) value;
                if (number.longValue() > annotation.value()) {
                    String message = !annotation.message().isEmpty()
                            ? annotation.message()
                            : MessageResolver.getMessage("error.max", field.getName(), annotation.value());
                    result.addError(new ValidationError(field.getName(), message, number));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void validateEmail(Object target, Field field, javax.validation.constraints.Email annotation, ValidationResult result) {
        try {
            Object value = field.get(target);
            if (value instanceof String) {
                String email = (String) value;
                String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
                if (!email.matches(EMAIL_REGEX)) {
                    String message = !annotation.message().isEmpty()
                            ? annotation.message()
                            : MessageResolver.getMessage("error.invalidEmail", field.getName());
                    result.addError(new ValidationError(field.getName(), message, email));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void validatePattern(Object target, Field field, javax.validation.constraints.Pattern annotation, ValidationResult result) {
        try {
            Object value = field.get(target);
            if (value instanceof String) {
                String str = (String) value;
                // In the standard annotation, the attribute is "regexp"
                String patternValue = annotation.regexp();
                if (!str.matches(patternValue)) {
                    String message = !annotation.message().isEmpty()
                            ? annotation.message()
                            : MessageResolver.getMessage("error.pattern", field.getName());
                    result.addError(new ValidationError(field.getName(), message, str));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}