package com.danielmorales.validatorx.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Annotation to indicate that a field must not be null.
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * public class Customer {
 *     @NotNull(message = "Customer name is required")
 *     private String name;
 * }
 * }
 * </pre>
 *
 * <p>The default validation message key is {@code error.notNull}.
 *
 * @author Daniel Morales
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotNull {
    /**
     * Specifies the message key for localization.
     *
     * @return the message key (default: "error.notNull")
     */
    String messageKey() default "error.notNull";

    /**
     * Specifies a custom validation message.
     *
     * @return the custom validation message (default: empty)
     */
    String message() default "";
}
