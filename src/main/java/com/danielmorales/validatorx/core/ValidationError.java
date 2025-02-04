package com.danielmorales.validatorx.core;

public class ValidationError {
    private final String fieldName;
    private final String message;
    private final Object invalidValue;

    public ValidationError(String fieldName, String message, Object invalidValue) {
        this.fieldName = fieldName;
        this.message = message;
        this.invalidValue = invalidValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }

    @Override
    public String toString() {
        return String.format("ValidationError[field=%s, message=%s, value=%s]",
                fieldName, message, invalidValue);
    }
}
