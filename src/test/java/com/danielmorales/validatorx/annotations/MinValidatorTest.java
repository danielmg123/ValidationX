package com.danielmorales.validatorx.annotations;

import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.core.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MinValidatorTest {

    /**
     * Simple model class for testing @Min(10).
     */
    static class MinTestModel {
        @Min(10)
        private int number;

        public MinTestModel(int number) {
            this.number = number;
        }
    }

    @Test
    void whenValueIsBelowMin_shouldFailValidation() {
        MinTestModel model = new MinTestModel(5);
        ValidationResult result = Validator.check(model).validate();

        assertTrue(result.hasErrors(), "Expected validation errors when value is below min");
        assertEquals(1, result.getErrors().size(), "Expected exactly one validation error");
        assertEquals("number", result.getErrors().get(0).getFieldName(), "Error should be tied to the 'number' field");
    }

    @Test
    void whenValueIsEqualToMin_shouldPassValidation() {
        MinTestModel model = new MinTestModel(10);
        ValidationResult result = Validator.check(model).validate();

        assertFalse(result.hasErrors(), "Did not expect validation errors when value is equal to min");
    }

    @Test
    void whenValueIsAboveMin_shouldPassValidation() {
        MinTestModel model = new MinTestModel(15);
        ValidationResult result = Validator.check(model).validate();

        assertFalse(result.hasErrors(), "Did not expect validation errors when value is above min");
    }
}