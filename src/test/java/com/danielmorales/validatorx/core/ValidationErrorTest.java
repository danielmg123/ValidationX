package com.danielmorales.validatorx.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationErrorTest {

    @Test
    void testValidationErrorFieldsAndToString() {
        ValidationError error = new ValidationError("myField", "Some error occurred", "badValue");

        assertEquals("myField", error.getFieldName(), "Field name should match constructor input");
        assertEquals("Some error occurred", error.getMessage(), "Message should match constructor input");
        assertEquals("badValue", error.getInvalidValue(), "Invalid value should match constructor input");

        String errorString = error.toString();
        assertTrue(errorString.contains("myField"), "toString should include field name");
        assertTrue(errorString.contains("Some error occurred"), "toString should include error message");
        assertTrue(errorString.contains("badValue"), "toString should include invalid value");
    }
}