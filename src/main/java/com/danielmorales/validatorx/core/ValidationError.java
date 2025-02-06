package com.danielmorales.validatorx.core;

/**
 * Represents a validation error that occurs when an input field fails validation.
 *
 * <p>Usage Example:
 * <pre>
 * {@code
 * ValidationError error = new ValidationError("email", "Invalid email format", "not-an-email");
 * System.out.println(error.getMessage()); // Prints: "Invalid email format"
 * }
 * </pre>
 *
 * @author Daniel Morales
 */
public class ValidationError {
    private final String fieldName;
    private final String message;
    private final Object invalidValue;

    /**
     * Constructs a {@code ValidationError}.
     *
     * @param fieldName the name of the field that failed validation
     * @param message the validation error message
     * @param invalidValue the value that caused the validation error
     */
    public ValidationError(String fieldName, String message, Object invalidValue) {
        this.fieldName = fieldName;
        this.message = message;
        this.invalidValue = invalidValue;
    }

    /**
     * @return the name of the field that failed validation
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @return the validation error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the invalid value that caused the error
     */
    public Object getInvalidValue() {
        return invalidValue;
    }

    @Override
    public String toString() {
        return String.format("ValidationError[field=%s, message=%s, value=%s]",
                fieldName, message, invalidValue);
    }
}
