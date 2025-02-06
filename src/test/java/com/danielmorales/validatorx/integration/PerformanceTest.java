package com.danielmorales.validatorx.integration;

import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.core.Validator;
import com.danielmorales.validatorx.model.User;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A very basic performance-related test demonstrating repeated runs 
 * and timing. (Not a vigorous benchmark)
 */
class PerformanceTest {

    @Test
    void testSingleValidationPerformance() {
        User user = new User("Daniel", "daniel@example.com", "password123");
        // Warm up
        for(int i = 0; i < 1000; i++) {
            Validator.check(user).validate();
        }

        long start = System.nanoTime();
        ValidationResult result = Validator.check(user).validate();
        long elapsed = System.nanoTime() - start;

        // We don't have a real threshold, but let's ensure it's not huge
        // For example, we assert it runs under 1 millisecond (1e6 ns) in a typical environment.
        assertTrue(elapsed < 1_000_000,
                   "Validation should typically complete in under 1 ms on a decent machine");
        assertFalse(result.hasErrors(), "This should pass validation as user data is valid");
    }

    @RepeatedTest(3)
    void testBulkValidationPerformance() {
        // Validate many random users in a loop to see if performance remains acceptable
        long start = System.currentTimeMillis();

        for (int i = 0; i < 10_000; i++) {
            User user = new User("User" + i, "test" + i + "@example.com", "Pass" + i + "word");
            ValidationResult result = Validator.check(user).validate();
            result.hasErrors();// In real usage, we might accumulate or handle them
        }

        long end = System.currentTimeMillis();
        long duration = end - start;

        // Arbitrary threshold for demonstration: 2 seconds for 10k validations
        assertTrue(duration < 2000,
                   "Validating 10,000 simple objects should finish in under 2 seconds on a typical modern machine");
    }
}