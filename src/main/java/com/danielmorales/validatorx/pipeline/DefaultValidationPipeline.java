package com.danielmorales.validatorx.pipeline;

import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.core.Validator;

import java.util.function.Consumer;

/**
 * Example pipeline implementation:
 *   ValidatorPipeline<MyUser> pipeline = new DefaultValidationPipeline<>();
 *   pipeline.validateRequest(user)
 *           .withRuleSet("signupRules")
 *           .onFailure(result -> { 
 *               // e.g. return a 400 HTTP response
 *           })
 *           .onSuccess(() -> { 
 *               // proceed
 *           })
 *           .execute();
 */
public class DefaultValidationPipeline<T> implements ValidationPipeline<T> {
    private T request;
    private String ruleSetName;
    private Consumer<ValidationResult> failureAction;
    private Runnable successAction;

    @Override
    public ValidationPipeline<T> validateRequest(T request) {
        this.request = request;
        return this;
    }

    @Override
    public ValidationPipeline<T> withRuleSet(String ruleSetName) {
        this.ruleSetName = ruleSetName;
        return this;
    }

    @Override
    public ValidationPipeline<T> onFailure(Consumer<ValidationResult> action) {
        this.failureAction = action;
        return this;
    }

    @Override
    public ValidationPipeline<T> onSuccess(Runnable action) {
        this.successAction = action;
        return this;
    }

    @Override
    public void execute() {
        // 1. Could run annotation-based or fluent checks here
        ValidationResult result = Validator.check(request).validate();

        // 2. If theres a "rule set," might define more programmatic checks or custom rules
        if (ruleSetName != null) {
            // e.g. apply some pre-configured rules from my own library
            // Define "rule sets"
        }

        // 3. Evaluate
        if (result.hasErrors()) {
            if (failureAction != null) {
                failureAction.accept(result);
            }
        } else {
            if (successAction != null) {
                successAction.run();
            }
        }
    }
}