package com.danielmorales.validatorx.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationResultTest {

    @Test
    void testAddErrorAndHasErrors() {
        ValidationResult result = new ValidationResult();
        assertFalse(result.hasErrors(), "Initially, result should have no errors");

        ValidationError error = new ValidationError("field1", "Something went wrong", "invalid");
        result.addError(error);

        assertTrue(result.hasErrors(), "Now result should have errors");
        assertEquals(1, result.getErrors().size(), "Should contain exactly one error");
        assertSame(error, result.getErrors().get(0), "Error object should match the one added");
    }
}