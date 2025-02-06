package com.danielmorales.validatorx.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    void testValidationExceptionStoresValidationResult() {
        ValidationResult result = new ValidationResult();
        result.addError(new ValidationError("fieldA", "Error message", "bad value"));

        ValidationException exception = new ValidationException("Validation failed", result);

        assertEquals("Validation failed", exception.getMessage(), 
                "Exception message should match constructor input");
        assertNotNull(exception.getValidationResult(), 
                "ValidationResult should not be null");
        assertTrue(exception.getValidationResult().hasErrors(), 
                "ValidationResult inside exception should have errors");
    }
}