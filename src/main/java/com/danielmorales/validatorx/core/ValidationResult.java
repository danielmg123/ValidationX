package com.danielmorales.validatorx.core;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private final List<ValidationError> errors = new ArrayList<>();

    public void addError(ValidationError error) {
        errors.add(error);
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
