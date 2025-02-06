package com.danielmorales.validatorx.model;

import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.core.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {

    @Test
    void testUserValidation_withInvalidData() {
        // name = null => @NotNull fails
        // email = "invalidEmail" => @Email fails
        // password = "short" => @Size(min=8) fails
        User user = new User(null, "invalidEmail", "short");

        ValidationResult result = Validator.check(user).validate();
        assertTrue(result.hasErrors(), "Expected errors because user data is invalid");
        assertEquals(3, result.getErrors().size(), "Should have errors for name, email, and password");
    }

    @Test
    void testUserValidation_withValidData() {
        // All fields should meet annotation constraints
        User user = new User("Daniel", "daniel@example.com", "longEnoughPassword");

        ValidationResult result = Validator.check(user).validate();
        assertFalse(result.hasErrors(), "No validation errors expected for valid user data");
    }

    @Test
    void testUserValidation_customizeOneField() {
        // Possibly changing only one field to be invalid
        User user = new User("Daniel", "noAtSymbol", "validpassword");

        ValidationResult result = Validator.check(user).validate();
        assertTrue(result.hasErrors());
        assertEquals(1, result.getErrors().size(), 
                "Only the email field should fail because it doesn't contain '@' or '.'");
    }
}