package com.danielmorales.validatorx.annotations;

import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.core.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MaxValidatorTest {

    /**
     * Simple model class for testing @Max(100).
     */
    static class MaxTestModel {
        @Max(100)
        private int number;

        public MaxTestModel(int number) {
            this.number = number;
        }
    }

    @Test
    void whenValueIsAboveMax_shouldFailValidation() {
        MaxTestModel model = new MaxTestModel(101);
        ValidationResult result = Validator.check(model).validate();

        assertTrue(result.hasErrors(), "Expected validation errors when value is above max");
        assertEquals(1, result.getErrors().size(), "Expected exactly one validation error");
        assertEquals("number", result.getErrors().get(0).getFieldName(), "Error should be tied to the 'number' field");
    }

    @Test
    void whenValueIsEqualToMax_shouldPassValidation() {
        MaxTestModel model = new MaxTestModel(100);
        ValidationResult result = Validator.check(model).validate();

        assertFalse(result.hasErrors(), "Did not expect validation errors when value is equal to max");
    }

    @Test
    void whenValueIsBelowMax_shouldPassValidation() {
        MaxTestModel model = new MaxTestModel(50);
        ValidationResult result = Validator.check(model).validate();

        assertFalse(result.hasErrors(), "Did not expect validation errors when value is below max");
    }
}