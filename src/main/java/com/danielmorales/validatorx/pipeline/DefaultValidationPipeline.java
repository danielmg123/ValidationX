package com.danielmorales.validatorx.pipeline;

import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.core.Validator;
import com.danielmorales.validatorx.core.ValidationProfileRegistry;

import java.util.function.Consumer;

/**
 * Default implementation of the {@link ValidationPipeline} interface.
 * This class provides a flexible validation pipeline that allows validation requests,
 * the application of rule sets, and customizable success and failure handling.
 *
 * @param <T> the type of the object being validated
 */
public class DefaultValidationPipeline<T> implements ValidationPipeline<T> {
    private T request;
    private String ruleSetName;
    private Consumer<ValidationResult> failureAction;
    private Runnable successAction;

    /**
     * Sets the object to be validated.
     *
     * @param request the request object to validate
     * @return the current pipeline instance for method chaining
     */
    @Override
    public ValidationPipeline<T> validateRequest(T request) {
        this.request = request;
        return this;
    }

    /**
     * Specifies a named rule set to be applied during validation.
     * Rule sets must be registered in {@link ValidationProfileRegistry}.
     *
     * @param ruleSetName the name of the rule set to apply
     * @return the current pipeline instance for method chaining
     */
    @Override
    public ValidationPipeline<T> withRuleSet(String ruleSetName) {
        this.ruleSetName = ruleSetName;
        return this;
    }

    /**
     * Defines a callback function to be executed if validation fails.
     *
     * @param action a consumer that receives the {@link ValidationResult} containing errors
     * @return the current pipeline instance for method chaining
     */
    @Override
    public ValidationPipeline<T> onFailure(Consumer<ValidationResult> action) {
        this.failureAction = action;
        return this;
    }

    /**
     * Defines a callback function to be executed if validation succeeds.
     *
     * @param action a {@link Runnable} to execute on successful validation
     * @return the current pipeline instance for method chaining
     */
    @Override
    public ValidationPipeline<T> onSuccess(Runnable action) {
        this.successAction = action;
        return this;
    }

    /**
     * Executes the validation pipeline.
     * It first performs annotation-based and fluent validation, applies any specified rule set,
     * and then triggers the appropriate callback based on the validation result.
     */
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
