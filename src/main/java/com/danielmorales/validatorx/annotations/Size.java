package com.danielmorales.validatorx.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Indicates that the annotated field’s length must be within a given range.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Size {
    int min() default 0;
    int max() default Integer.MAX_VALUE;

    String messageKey() default "error.size";
    String message() default "";
}
