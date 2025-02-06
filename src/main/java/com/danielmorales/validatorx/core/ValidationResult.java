package com.danielmorales.validatorx.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of a validation process, containing validation errors if any exist.
 *
 * <p>Usage Example:
 * <pre>
 * {@code
 * ValidationResult result = new ValidationResult();
 * result.addError(new ValidationError("email", "Invalid email format", "not-an-email"));
 *
 * if (result.hasErrors()) {
 *     System.out.println("Validation failed: " + result.getErrors());
 * }
 * }
 * </pre>
 *
 * @author Daniel Morales
 */
public class ValidationResult {
    private final List<ValidationError> errors = new ArrayList<>();

    /**
     * Adds a validation error to the result.
     *
     * @param error the validation error to add
     */
    public void addError(ValidationError error) {
        errors.add(error);
    }

    /**
     * @return the list of validation errors
     */
    public List<ValidationError> getErrors() {
        return errors;
    }

    /**
     * @return {@code true} if there are validation errors, {@code false} otherwise
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
