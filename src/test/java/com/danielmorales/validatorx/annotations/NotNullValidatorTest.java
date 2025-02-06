package com.danielmorales.validatorx.annotations;

import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.core.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotNullValidatorTest {

    /**
     * Simple model class for testing @NotNull.
     */
    static class NotNullTestModel {
        @NotNull
        private String text;

        public NotNullTestModel(String text) {
            this.text = text;
        }
    }

    @Test
    void whenFieldIsNull_shouldFailValidation() {
        NotNullTestModel model = new NotNullTestModel(null);
        ValidationResult result = Validator.check(model).validate();

        assertTrue(result.hasErrors(), "Expected validation errors when @NotNull field is null");
        assertEquals(1, result.getErrors().size(), "Expected exactly one validation error");
        assertEquals("text", result.getErrors().get(0).getFieldName(), "Error should be tied to the 'text' field");
    }

    @Test
    void whenFieldIsNotNull_shouldPassValidation() {
        NotNullTestModel model = new NotNullTestModel("Non-null value");
        ValidationResult result = Validator.check(model).validate();

        assertFalse(result.hasErrors(), "Did not expect any validation errors for non-null field");
    }
}