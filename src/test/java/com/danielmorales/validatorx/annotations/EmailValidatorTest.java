package com.danielmorales.validatorx.annotations;

import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.core.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    /**
     * Simple model class for testing @Email.
     */
    static class EmailTestModel {
        @Email
        private String email;

        public EmailTestModel(String email) {
            this.email = email;
        }
    }

    @Test
    void whenEmailIsInvalid_shouldFailValidation() {
        EmailTestModel model = new EmailTestModel("noAtOrDot");
        ValidationResult result = Validator.check(model).validate();

        assertTrue(result.hasErrors(), "Expected validation errors for invalid email");
        assertEquals(1, result.getErrors().size(), "Expected exactly one validation error");
        assertEquals("email", result.getErrors().get(0).getFieldName(), "Error should be tied to the 'email' field");
    }

    @Test
    void whenEmailIsValid_shouldPassValidation() {
        EmailTestModel model = new EmailTestModel("test@example.com");
        ValidationResult result = Validator.check(model).validate();

        assertFalse(result.hasErrors(), "Did not expect validation errors for a valid email");
    }

    @Test
    void whenUsingCustomRegex_shouldEnforceIt() {
        class CustomEmailModel {
            @Email(regex = "^[A-Za-z0-9._%+-]+@mycompany\\.com$")
            private String corporateEmail;
            CustomEmailModel(String email) { this.corporateEmail = email; }
        }

        // Invalid: does not end with @mycompany.com
        CustomEmailModel invalidModel = new CustomEmailModel("user@anothercompany.com");
        ValidationResult invalidResult = Validator.check(invalidModel).validate();
        assertTrue(invalidResult.hasErrors(), "Should fail because custom regex does not match");

        // Valid: ends with @mycompany.com
        CustomEmailModel validModel = new CustomEmailModel("jane@mycompany.com");
        ValidationResult validResult = Validator.check(validModel).validate();
        assertFalse(validResult.hasErrors(), "Should pass custom regex requirement");
    }

}