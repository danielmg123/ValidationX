package com.danielmorales.validatorx.pipeline;

import com.danielmorales.validatorx.core.ValidationProfileRegistry;
import com.danielmorales.validatorx.core.ValidationResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultValidationPipelineTest {

    // A simple request object for demonstration
    static class Request {
        String input;
        public Request(String input) { this.input = input; }
        public String getInput() { return input; }
        public void setInput(String input) { this.input = input; }
    }

    @BeforeAll
    static void setupProfiles() {
        // Register a simple custom profile if needed
        // This profile requires that the "input" field has a length >= 5
        ValidationProfileRegistry.registerProfile("minLength5", builder -> {
            builder.isNotNull("input", "Input must not be null")
                   .hasLengthBetween("input", 5, 50, "Input must be at least 5 chars");
        });
    }

    @Test
    void testPipeline_withFailure() {
        Request request = new Request("abc"); // Too short
        ValidationResult[] capturedResult = new ValidationResult[1];

        // Create the pipeline with a rule set that enforces minLength5
        new DefaultValidationPipeline<Request>()
            .validateRequest(request)
            .withRuleSet("minLength5")
            .onFailure(result -> {
                capturedResult[0] = result; // capture the result
            })
            .onSuccess(() -> fail("Success callback should not be called with invalid data"))
            .execute();

        assertNotNull(capturedResult[0], "Failure action should be invoked");
        assertTrue(capturedResult[0].hasErrors(), "Should contain validation errors");
        assertEquals(1, capturedResult[0].getErrors().size(), 
                "Expecting exactly 1 error about length");
    }

    @Test
    void testPipeline_withSuccess() {
        Request request = new Request("HelloWorld"); // 10 chars

        // We'll track callbacks via booleans or counters
        final boolean[] successCalled = { false };
        final boolean[] failureCalled = { false };

        new DefaultValidationPipeline<Request>()
            .validateRequest(request)
            .withRuleSet("minLength5")
            .onFailure(result -> failureCalled[0] = true)
            .onSuccess(() -> successCalled[0] = true)
            .execute();

        assertFalse(failureCalled[0], "Failure callback should not be called for valid data");
        assertTrue(successCalled[0], "Success callback should be triggered for valid data");
    }

    @Test
    void testPipeline_noRuleSet() {
        // If no rule set is specified, only annotation-based & fluent validations apply
        // Suppose our request is fine since there's no annotation to check
        Request request = new Request("abc");

        final boolean[] successCalled = { false };
        final boolean[] failureCalled = { false };

        new DefaultValidationPipeline<Request>()
            .validateRequest(request)
            // No ruleSet
            .onFailure(result -> failureCalled[0] = true)
            .onSuccess(() -> successCalled[0] = true)
            .execute();

        // By default, annotation checks won't do anything (the Request class is not annotated).
        // If no fluent validations are applied, the result should have no errors.
        assertFalse(failureCalled[0], "No errors expected since there's no rule set or annotations");
        assertTrue(successCalled[0], "Success callback should be triggered by default in this scenario");
    }
}