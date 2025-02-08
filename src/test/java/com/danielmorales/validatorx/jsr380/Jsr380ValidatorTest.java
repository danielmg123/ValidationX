package com.danielmorales.validatorx.jsr380;

import com.danielmorales.validatorx.core.ValidationError;
import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.jsr380.Jsr380Validator;
import org.junit.jupiter.api.Test;
import com.danielmorales.validatorx.core.Validator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the new API method that integrates JSR 380 validations.
 */
class Jsr380ValidatorTest {

    /**
     * A test model that uses both JSR 380 annotations (javax.validation.constraints.*)
     * and custom ValidatorX annotations.
     */
    static class TestModel {
        // Standard JSR 380 annotation
        @javax.validation.constraints.NotNull(message = "JSR NotNull: jsrField must not be null")
        String jsrField;

        // Custom ValidatorX annotation
        @com.danielmorales.validatorx.annotations.NotNull(message = "Custom NotNull: customField must not be null")
        String customField;

        // Standard JSR 380 annotation for email
        @javax.validation.constraints.Email(message = "JSR Email: must be a valid email")
        String jsrEmail;

        TestModel(String jsrField, String customField, String jsrEmail) {
            this.jsrField = jsrField;
            this.customField = customField;
            this.jsrEmail = jsrEmail;
        }
    }

    /**
     * Test case where all fields are valid.
     * No errors should be present.
     */
    @Test
    void testValidatorWithJsr380_validData() {
        TestModel model = new TestModel("value", "non-null", "test@example.com");
        ValidationResult result = Validator.checkWithJsr380(model);
        assertFalse(result.hasErrors(), "Expected no errors when all fields are valid");
    }

    /**
     * Test case where JSR 380 validations fail:
     * - jsrField is null (violates @NotNull)
     * - jsrEmail is invalid (violates @Email)
     * The custom annotation on customField should pass.
     */
    @Test
    void testValidatorWithJsr380_invalidData_jsrNotNullAndEmail() {
        TestModel model = new TestModel(null, "non-null", "invalid-email");
        ValidationResult result = Validator.checkWithJsr380(model);
        assertTrue(result.hasErrors(), "Expected errors when JSR validations fail");

        // We expect two errors:
        // 1. For jsrField (JSR NotNull violation)
        // 2. For jsrEmail (JSR Email violation)
        assertEquals(2, result.getErrors().size(), "Expected exactly 2 errors");

        boolean hasJsNotNullError = result.getErrors().stream()
                .anyMatch(error -> "jsrField".equals(error.getFieldName()) &&
                        error.getMessage().contains("JSR NotNull"));
        boolean hasJsEmailError = result.getErrors().stream()
                .anyMatch(error -> "jsrEmail".equals(error.getFieldName()) &&
                        error.getMessage().contains("JSR Email"));

        assertTrue(hasJsNotNullError, "Expected error for jsrField not being null");
        assertTrue(hasJsEmailError, "Expected error for jsrEmail invalid format");
    }

    /**
     * Test case where the custom ValidatorX annotation fails:
     * customField is null.
     */
    @Test
    void testValidatorWithJsr380_invalidData_customNotNull() {
        TestModel model = new TestModel("value", null, "test@example.com");
        ValidationResult result = Validator.checkWithJsr380(model);
        assertTrue(result.hasErrors(), "Expected error when custom field is null");

        // We expect one error from the custom annotation on customField.
        assertEquals(1, result.getErrors().size(), "Expected exactly one error");

        ValidationError error = result.getErrors().get(0);
        assertEquals("customField", error.getFieldName(), "Error should be for customField");
        assertTrue(error.getMessage().contains("Custom NotNull"), "Expected custom error message");
    }
}