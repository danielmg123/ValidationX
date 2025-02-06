package com.danielmorales.validatorx.annotations;

import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.core.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SizeValidatorTest {

    /**
     * Simple model class for testing @Size with min=8, max=20.
     */
    static class SizeTestModel {
        @Size(min = 8, max = 20)
        private String password;

        public SizeTestModel(String password) {
            this.password = password;
        }
    }

    @Test
    void whenStringIsTooShort_shouldFailValidation() {
        SizeTestModel model = new SizeTestModel("short");
        ValidationResult result = Validator.check(model).validate();

        assertTrue(result.hasErrors(), "Expected validation errors for too short string");
        assertEquals(1, result.getErrors().size(), "Expected exactly one validation error");
        assertEquals("password", result.getErrors().get(0).getFieldName(), "Error should be tied to the 'password' field");
    }

    @Test
    void whenStringIsTooLong_shouldFailValidation() {
        // 21 characters
        String longString = "abcdefghijklmnopqrstu";
        SizeTestModel model = new SizeTestModel(longString);
        ValidationResult result = Validator.check(model).validate();

        assertTrue(result.hasErrors(), "Expected validation errors for too long string");
        assertEquals(1, result.getErrors().size(), "Expected exactly one validation error");
        assertEquals("password", result.getErrors().get(0).getFieldName(), "Error should be tied to the 'password' field");
    }

    @Test
    void whenStringIsWithinRange_shouldPassValidation() {
        // Exactly 8 characters or somewhere between 8 and 20
        SizeTestModel model = new SizeTestModel("validPass");
        ValidationResult result = Validator.check(model).validate();

        assertFalse(result.hasErrors(), "Did not expect validation errors for in-range string length");
    }
}