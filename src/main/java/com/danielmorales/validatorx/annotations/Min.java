package com.danielmorales.validatorx.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify the minimum allowable value for a numeric field.
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * public class Order {
 *     @Min(value = 1, message = "Quantity must be at least 1")
 *     private int quantity;
 * }
 * }
 * </pre>
 *
 * <p>The default validation message key is {@code error.min}.
 *
 * @author Daniel Morales
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Min {

    /**
     * Specifies the minimum allowable value.
     *
     * @return the minimum value
     */
    long value();

    /**
     * Specifies the message key for localization.
     *
     * @return the message key (default: "error.min")
     */
    String messageKey() default "error.min";

    /**
     * Specifies a custom validation message.
     *
     * @return the custom validation message (default: empty)
     */
    String message() default "";
}