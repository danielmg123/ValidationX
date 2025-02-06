package com.danielmorales.validatorx.pipeline;

import com.danielmorales.validatorx.core.ValidationResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationPipelineTest {

    /**
     * Since ValidationPipeline is an interface, we'll define a small anonymous class
     * or mock to test basic interface chaining.
     */
    @Test
    void testInterfaceChain() {
        final boolean[] actionsCalled = {false, false};

        ValidationPipeline<String> pipeline = new ValidationPipeline<>() {
            private String request;
            private String ruleSetName;
            private Runnable successAction;
            private java.util.function.Consumer<ValidationResult> failureAction;

            @Override
            public ValidationPipeline<String> validateRequest(String request) {
                this.request = request;
                return this;
            }

            @Override
            public ValidationPipeline<String> withRuleSet(String ruleSetName) {
                this.ruleSetName = ruleSetName;
                return this;
            }

            @Override
            public ValidationPipeline<String> onFailure(java.util.function.Consumer<ValidationResult> action) {
                this.failureAction = action;
                return this;
            }

            @Override
            public ValidationPipeline<String> onSuccess(Runnable action) {
                this.successAction = action;
                return this;
            }

            @Override
            public void execute() {
                // For this demo, assume "valid" request if ruleSetName is null
                // or the request length > 5, else we fail
                ValidationResult result = new ValidationResult();
                if (ruleSetName == null || (request != null && request.length() > 5)) {
                    // success
                    if (successAction != null) successAction.run();
                    actionsCalled[0] = true; // track success
                } else {
                    // fail
                    if (failureAction != null) {
                        result.getErrors().add(new com.danielmorales.validatorx.core.ValidationError(
                            "mockField", "Mock failure", request));
                        failureAction.accept(result);
                    }
                    actionsCalled[1] = true; // track failure
                }
            }
        };

        // Chain calls in a single expression
        pipeline.validateRequest("validData")
                .withRuleSet(null)
                .onFailure(r -> fail("Should not call onFailure for valid data"))
                .onSuccess(() -> {})
                .execute();

        assertTrue(actionsCalled[0], "Success was called");
        assertFalse(actionsCalled[1], "Failure was not called");
    }
}