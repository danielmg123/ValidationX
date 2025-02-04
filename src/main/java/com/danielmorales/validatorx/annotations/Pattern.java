package com.danielmorales.validatorx.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Indicates that the annotated field must match a specified regex pattern.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Pattern {
    String regex();
    String messageKey() default "error.pattern";
    String message() default "";
}