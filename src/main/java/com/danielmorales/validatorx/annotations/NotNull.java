package com.danielmorales.validatorx.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Indicates that the annotated field must not be null.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotNull {
    /**
     * Custom message key for localization purposes.
     * Example usage: "error.notNull"
     */
    String messageKey() default "error.notNull";

    /**
     * Optional custom message to override localization.
     * Example usage: "Field cannot be null!"
     */
    String message() default "";
}
