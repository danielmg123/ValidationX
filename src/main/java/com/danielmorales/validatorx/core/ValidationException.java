package com.danielmorales.validatorx.core;

public class ValidationException extends RuntimeException {
    private final ValidationResult validationResult;

    public ValidationException(String message, ValidationResult result) {
        super(message);
        this.validationResult = result;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }
}