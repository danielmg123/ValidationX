package com.danielmorales.validatorx.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Annotation to specify the maximum allowable value for a numeric field.
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * public class Product {
 *     @Max(value = 100, message = "Price must be at most 100")
 *     private double price;
 * }
 * }
 * </pre>
 *
 * <p>The default validation message key is {@code error.max}.
 *
 * @author Daniel Morales
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Max {
    /**
     * Specifies the maximum allowable value.
     *
     * @return the maximum value
     */
    long value();

    /**
     * Specifies the message key for localization.
     *
     * @return the message key (default: "error.max")
     */
    String messageKey() default "error.max";

    /**
     * Specifies a custom validation message.
     *
     * @return the custom validation message (default: empty)
     */
    String message() default "";
}
