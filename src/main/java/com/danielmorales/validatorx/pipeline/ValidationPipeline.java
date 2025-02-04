package com.danielmorales.validatorx.pipeline;

import com.danielmorales.validatorx.core.ValidationResult;

import java.util.function.Consumer;

public interface ValidationPipeline<T> {
    ValidationPipeline<T> validateRequest(T request);
    ValidationPipeline<T> withRuleSet(String ruleSetName);
    ValidationPipeline<T> onFailure(Consumer<ValidationResult> action);
    ValidationPipeline<T> onSuccess(Runnable action);

    void execute();
}