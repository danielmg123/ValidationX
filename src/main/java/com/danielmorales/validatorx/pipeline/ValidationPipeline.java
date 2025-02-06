package com.danielmorales.validatorx.pipeline;

import com.danielmorales.validatorx.core.ValidationResult;

import java.util.function.Consumer;

/**
 * Represents a validation pipeline that enables structured validation workflows.
 * <p>
 * This interface defines a fluent API for handling validation with rule sets,
 * success callbacks, and failure actions.
 *
 * @param <T> The type of object to validate.
 */
public interface ValidationPipeline<T> {

    /**
     * Specifies the request object that needs validation.
     *
     * @param request the object to validate.
     * @return the current instance of {@code ValidationPipeline}.
     */
    ValidationPipeline<T> validateRequest(T request);

    /**
     * Associates a named rule set with this validation pipeline.
     * <p>
     * The rule set must be previously registered in {@code ValidationProfileRegistry}.
     *
     * @param ruleSetName the name of the validation rule set.
     * @return the current instance of {@code ValidationPipeline}.
     */
    ValidationPipeline<T> withRuleSet(String ruleSetName);

    /**
     * Specifies an action to execute if validation fails.
     * <p>
     * The provided {@code Consumer} will receive the {@link ValidationResult}
     * containing all validation errors.
     *
     * @param action the action to execute upon failure.
     * @return the current instance of {@code ValidationPipeline}.
     */
    ValidationPipeline<T> onFailure(Consumer<ValidationResult> action);

    /**
     * Specifies an action to execute if validation succeeds.
     * <p>
     * The provided {@code Runnable} will be executed when no validation errors are found.
     *
     * @param action the action to execute upon success.
     * @return the current instance of {@code ValidationPipeline}.
     */
    ValidationPipeline<T> onSuccess(Runnable action);

    /**
     * Executes the validation pipeline.
     * <p>
     * This method performs the validation, checks for errors, and triggers
     * either the failure or success action accordingly.
     */
    void execute();
}