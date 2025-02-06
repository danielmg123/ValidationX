package com.danielmorales.validatorx.core;

/**
 * Represents an exception that is thrown when validation fails.
 *
 * <p>Usage Example:
 * <pre>
 * {@code
 * ValidationResult result = new ValidationResult();
 * result.addError(new ValidationError("username", "Username is required", null));
 *
 * if (result.hasErrors()) {
 *     throw new ValidationException("Validation failed", result);
 * }
 * }
 * </pre>
 *
 * @author Daniel Morales
 */
public class ValidationException extends RuntimeException {
    private final ValidationResult validationResult;

    /**
     * Constructs a {@code ValidationException} with a message and a {@code ValidationResult}.
     *
     * @param message the exception message
     * @param result the validation result containing validation errors
     */
    public ValidationException(String message, ValidationResult result) {
        super(message);
        this.validationResult = result;
    }

    /**
     * @return the validation result associated with this exception
     */
    public ValidationResult getValidationResult() {
        return validationResult;
    }
}