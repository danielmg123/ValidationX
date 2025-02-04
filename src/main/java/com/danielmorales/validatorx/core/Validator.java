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

        // Can optionally allow turning off annotation scanning for purely fluent checks
        public ValidationBuilder skipAnnotations() {
            this.includeAnnotations = false;
            return this;
        }

        // For each programmatic check
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

        // Add more checks: matchesRegex, hasLengthBetween, etc...

        // 3. Main validate method
        public ValidationResult validate() {
            ValidationResult result = new ValidationResult();

            // If annotation scanning is on, run the ValidatorEngine
            if (includeAnnotations) {
                ValidatorEngine engine = new ValidatorEngine();
                ValidationResult annotationResult = engine.validate(target);
                result.getErrors().addAll(annotationResult.getErrors());
            }

            // Add fluent builder errors
            result.getErrors().addAll(errors);

            return result;
        }

        // Reflection helper
        private Object getFieldValue(String fieldName) throws IllegalAccessException, NoSuchFieldException {
            Class<?> clazz = target.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        }
    }
}