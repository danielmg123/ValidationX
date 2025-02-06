package com.danielmorales.validatorx.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Annotation to enforce that a field's value matches a specified regular expression pattern.
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * public class User {
 *     @Pattern(regex = "^[a-zA-Z0-9]{5,10}$", message = "Username must be 5-10 alphanumeric characters")
 *     private String username;
 * }
 * }
 * </pre>
 *
 * <p>The default validation message key is {@code error.pattern}.
 *
 * @author Daniel Morales
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Pattern {
    /**
     * Specifies the regex pattern to match.
     *
     * @return the regular expression pattern
     */
    String regex();

    /**
     * Specifies the message key for localization.
     *
     * @return the message key (default: "error.pattern")
     */
    String messageKey() default "error.pattern";

    /**
     * Specifies a custom validation message.
     *
     * @return the custom validation message (default: empty)
     */
    String message() default "";
}