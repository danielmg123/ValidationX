package com.danielmorales.validatorx.core;

import com.danielmorales.validatorx.annotations.Email;
import com.danielmorales.validatorx.annotations.Min;
import com.danielmorales.validatorx.annotations.NotNull;
import com.danielmorales.validatorx.annotations.Size;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorEngineTest {

    /**
     * A small test model for annotation-based validation.
     */
    static class EngineTestModel {
        @NotNull
        String mustNotBeNull;

        @Email
        String mustBeEmail;

        @Size(min = 3, max = 5)
        String sizeLimited;

        @Min(10)
        int minimumValue;

        EngineTestModel(String mustNotBeNull, String mustBeEmail, String sizeLimited, int minimumValue) {
            this.mustNotBeNull = mustNotBeNull;
            this.mustBeEmail = mustBeEmail;
            this.sizeLimited = sizeLimited;
            this.minimumValue = minimumValue;
        }
    }

    @Test
    void testAccumulateValidate_withMultipleViolations() {
        // mustNotBeNull = null -> fails @NotNull
        // mustBeEmail = "invalid" -> fails @Email
        // sizeLimited = "tooLong" -> fails @Size(3..5)
        // minimumValue = 5 -> fails @Min(10)

        EngineTestModel model = new EngineTestModel(null, "invalid", "tooLong", 5);

        ValidatorEngine engine = new ValidatorEngine();
        ValidationResult result = engine.accumulateValidate(model);

        assertTrue(result.hasErrors(), "Expected multiple validation errors");
        assertEquals(4, result.getErrors().size(), "All four fields should fail");
    }

    @Test
    void testAccumulateValidate_withValidData() {
        EngineTestModel model = new EngineTestModel("NotNull", "test@example.com", "okay", 15);

        ValidatorEngine engine = new ValidatorEngine();
        ValidationResult result = engine.accumulateValidate(model);

        assertFalse(result.hasErrors(), "Valid model should have no validation errors");
    }

    @Test
    void testValidateAndThrow_throwsWhenInvalid() {
        EngineTestModel model = new EngineTestModel(null, "valid@email", "ok", 12);

        ValidatorEngine engine = new ValidatorEngine();
        // Because mustNotBeNull is null, we expect a ValidationException
        assertThrows(ValidationException.class, () -> engine.validateAndThrow(model),
                "Should throw ValidationException for null field");
    }

    @Test
    void testValidateAndThrow_noThrowWhenValid() {
        EngineTestModel model = new EngineTestModel("Hello", "me@domain.com", "okay", 20); // "okay" has 4 characters
    
        ValidatorEngine engine = new ValidatorEngine();
        assertDoesNotThrow(() -> engine.validateAndThrow(model),
                "No exception should be thrown for valid data");
    }
}