package com.danielmorales.validatorx.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Indicates that the annotated field must be a valid email address.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Email {
    String messageKey() default "error.invalidEmail";
    String message() default "";
}
