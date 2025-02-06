package com.danielmorales.validatorx.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    /**
     * A simple model for testing fluent validation on fields.
     */
    static class FluentTestModel {
        String name;
        String email;
        String password;
    }

    @Test
    void testFluentValidation_noAnnotations() {
        FluentTestModel model = new FluentTestModel();
        model.name = null;
        model.email = "invalid";
        model.password = "short";

        // We skipAnnotations to ensure purely fluent checks
        ValidationResult result = Validator.check(model)
                .skipAnnotations()
                .isNotNull("name", "Name must not be null")
                .isEmail("email", "Email is invalid")
                .hasLengthBetween("password", 8, 20, "Password must be between 8-20 chars")
                .validate();

        assertTrue(result.hasErrors(), "We expect multiple errors due to invalid data");
        assertEquals(3, result.getErrors().size(), "Name, email, and password should all fail");
    }

    @Test
    void testFluentValidation_validData() {
        FluentTestModel model = new FluentTestModel();
        model.name = "Daniel";
        model.email = "test@example.com";
        model.password = "longEnoughPassword";

        ValidationResult result = Validator.check(model)
                .skipAnnotations()
                .isNotNull("name", "Name must not be null")
                .isEmail("email", "Email is invalid")
                .hasLengthBetween("password", 8, 20, "")
                .validate();

        assertFalse(result.hasErrors(), "No errors should be present with valid data");
    }

    @Test
    void testFluentValidation_validateAndThrow() {
        FluentTestModel model = new FluentTestModel();
        model.name = null;  // invalid

        assertThrows(ValidationException.class, () -> {
            Validator.check(model)
                     .skipAnnotations()
                     .isNotNull("name", "Name must not be null")
                     .validateAndThrow();
        }, "Should throw ValidationException for invalid data");
    }

    @Test
    void testIncludeAnnotationsByDefault() {
        // If we don't skipAnnotations, it uses ValidatorEngine for annotation checks
        // We'll define a small annotated class inline:

        class AnnotatedModel {
            @com.danielmorales.validatorx.annotations.NotNull
            String field;
        }

        AnnotatedModel model = new AnnotatedModel();
        model.field = null;  // invalid for @NotNull

        ValidationResult result = Validator.check(model).validate();
        assertTrue(result.hasErrors(), "Annotation-based validation should catch null field");
        assertEquals(1, result.getErrors().size(), "Expect exactly one error for the null field");
    }
}