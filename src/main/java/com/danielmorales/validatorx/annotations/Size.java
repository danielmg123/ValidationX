package com.danielmorales.validatorx.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Annotation to specify a valid size range for a field, typically for strings or collections.
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * public class PasswordPolicy {
 *     @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
 *     private String password;
 * }
 * }
 * </pre>
 *
 * <p>The default validation message key is {@code error.size}.
 *
 * @author Daniel Morales
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Size {
    /**
     * Specifies the minimum allowable size.
     *
     * @return the minimum size (default: 0)
     */
    int min() default 0;

    /**
     * Specifies the maximum allowable size.
     *
     * @return the maximum size (default: {@code Integer.MAX_VALUE})
     */
    int max() default Integer.MAX_VALUE;

    /**
     * Specifies the message key for localization.
     *
     * @return the message key (default: "error.size")
     */
    String messageKey() default "error.size";

    /**
     * Specifies a custom validation message.
     *
     * @return the custom validation message (default: empty)
     */
    String message() default "";
}
