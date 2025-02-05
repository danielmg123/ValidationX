package com.danielmorales.validatorx.pipeline;

import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.core.Validator;
import com.danielmorales.validatorx.core.ValidationProfileRegistry;

import java.util.function.Consumer;

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
        // Run the base (annotation-based and fluent) validations.
        ValidationResult result = Validator.check(request).validate();

        // If a rule set (profile) is specified, look it up and apply it.
        if (ruleSetName != null) {
            Consumer<Validator.ValidationBuilder> profile = ValidationProfileRegistry.getProfile(ruleSetName);
            if (profile != null) {
                // Create a new builder for additional rules (skip annotations)
                Validator.ValidationBuilder profileBuilder = Validator.check(request).skipAnnotations();
                // Apply the profile validations
                profile.accept(profileBuilder);
                // Merge errors from the profile into the overall result.
                ValidationResult profileResult = profileBuilder.validate();
                result.getErrors().addAll(profileResult.getErrors());
            }
        }

        // Evaluate and trigger the appropriate callback.
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
