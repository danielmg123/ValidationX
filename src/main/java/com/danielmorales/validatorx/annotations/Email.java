package com.danielmorales.validatorx.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Annotation to validate that a field contains a properly formatted email address.
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * public class User {
 *     @Email(message = "Invalid email format")
 *     private String email;
 * }
 * }
 * </pre>
 *
 * <p>The default validation message key is {@code error.invalidEmail}.
 *
 * @author Daniel Morales
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Email {
    /**
     * Specifies the message key for localization.
     *
     * @return the message key (default: "error.invalidEmail")
     */
    String messageKey() default "error.invalidEmail";

    /**
     * Specifies a custom validation message.
     *
     * @return the custom validation message (default: empty)
     */
    String message() default "";

    /**
     * Optional custom regex for validating the email format more strictly.
     * If empty, the validator uses the default email-check logic.
     *
     * @return the custom regex pattern (default: empty string)
     */
    String regex() default "";
}
