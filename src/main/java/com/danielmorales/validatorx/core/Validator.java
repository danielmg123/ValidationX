package com.danielmorales.validatorx.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.danielmorales.validatorx.rules.RuleRegistry;

public class Validator {

    // 1. Entry point
    public static ValidationBuilder check(Object target) {
        return new ValidationBuilder(target);
    }

    // 2. Inner builder class
    public static class ValidationBuilder {
        private final Object target;
        private final List<ValidationError> errors = new ArrayList<>();
        private boolean includeAnnotations = true;

        public ValidationBuilder(Object target) {
            this.target = target;
        }

        // Optionally turn off annotation scanning for purely fluent checks
        public ValidationBuilder skipAnnotations() {
            this.includeAnnotations = false;
            return this;
        }

        // Fluent check: Not null
        public ValidationBuilder isNotNull(String fieldName, String customMsg) {
            try {
                Object value = getFieldValue(fieldName);
                if (value == null) {
                    errors.add(new ValidationError(fieldName, customMsg, null));
                }
            } catch (Exception e) {
                // handle reflection or no-such-field
            }
            return this;
        }

        // Fluent check: Email validation
        public ValidationBuilder isEmail(String fieldName, String customMsg) {
            try {
                Object value = getFieldValue(fieldName);
                if (value instanceof String) {
                    String str = (String) value;
                    if (!str.contains("@") || !str.contains(".")) {
                        errors.add(new ValidationError(fieldName, customMsg, str));
                    }
                }
            } catch (Exception e) {
                // handle reflection
            }
            return this;
        }

        // Fluent check: Apply a custom rule by name (registered in RuleRegistry)
        public ValidationBuilder applyRule(String ruleName, String fieldName, String customMsg) {
            try {
                Object value = getFieldValue(fieldName);
                Predicate<Object> rule = RuleRegistry.getRule(ruleName);
                if (rule != null) {
                    if (!rule.test(value)) {
                        errors.add(new ValidationError(fieldName, customMsg, value));
                    }
                } else {
                    // Possibly log or handle the case where the rule doesn't exist
                    errors.add(new ValidationError(fieldName,
                        "No rule found for: " + ruleName, value));
                }
            } catch (Exception e) {
                // reflection error or field not found
            }
            return this;
        }

        // Validate that a String's length is within a given range
        public ValidationBuilder hasLengthBetween(String fieldName, int min, int max, String customMsg) {
            try {
                Object value = getFieldValue(fieldName);
                if (value instanceof String) {
                    int length = ((String) value).length();
                    if (length < min || length > max) {
                        errors.add(new ValidationError(fieldName,
                            customMsg.isEmpty() ? String.format("Length must be between %d and %d", min, max) : customMsg,
                            value));
                    }
                }
            } catch (Exception e) {
                // Handle reflection exceptions
            }
            return this;
        }

        // Validate that a String field matches a given regex
        public ValidationBuilder matchesRegex(String fieldName, String regex, String customMsg) {
            try {
                Object value = getFieldValue(fieldName);
                if (value instanceof String) {
                    String str = (String) value;
                    if (!str.matches(regex)) {
                        errors.add(new ValidationError(fieldName,
                            customMsg.isEmpty() ? String.format("Field '%s' must match regex '%s'", fieldName, regex) : customMsg,
                            value));
                    }
                }
            } catch (Exception e) {
                // Handle reflection exceptions
            }
            return this;
        }

        // Cross-field validation using a custom rule (for complex business logic)
        public ValidationBuilder customRule(Predicate<Object> rule, String customMsg) {
            if (!rule.test(target)) {
                errors.add(new ValidationError("object", customMsg, target));
            }
            return this;
        }

        // Main validate method (accumulates errors)
        public ValidationResult validate() {
            ValidationResult result = new ValidationResult();

            // If annotation scanning is enabled, run the ValidatorEngine validations
            if (includeAnnotations) {
                ValidatorEngine engine = new ValidatorEngine();
                // Use accumulateValidate instead of validate to avoid throwing exceptions immediately
                ValidationResult annotationResult = engine.accumulateValidate(target);
                result.getErrors().addAll(annotationResult.getErrors());
            }

            // Add fluent builder errors
            result.getErrors().addAll(errors);

            return result;
        }

        /**
         * Performs validation and immediately throws a ValidationException if any errors are found.
         *
         * @return ValidationResult if no errors are present
         * @throws ValidationException if there are validation errors
         */
        public ValidationResult validateAndThrow() {
            ValidationResult result = validate();
            if (result.hasErrors()) {
                throw new ValidationException("Validation failed", result);
            }
            return result;
        }

        // Reflection helper method to get the value of a given field name
        private Object getFieldValue(String fieldName) throws IllegalAccessException, NoSuchFieldException {
            Class<?> clazz = target.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        }
    }
}