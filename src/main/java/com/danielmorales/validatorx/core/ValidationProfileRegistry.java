package com.danielmorales.validatorx.core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A registry for grouping additional validations into named profiles (or rule sets).
 * Profiles are defined as {@code Consumer} functions that accept a {@code ValidationBuilder}.
 *
 * <p>Usage Example:
 * <pre>
 * {@code
 * ValidationProfileRegistry.registerProfile("signup", builder -> {
 *     builder.isNotNull("email", "Email is required")
 *            .isEmail("email", "Invalid email format");
 * });
 *
 * Validator.ValidationBuilder builder = Validator.check(user);
 * ValidationProfileRegistry.getProfile("signup").accept(builder);
 * ValidationResult result = builder.validate();
 * }
 * </pre>
 *
 * @author Daniel Morales
 */
public class ValidationProfileRegistry {
    private static final Map<String, Consumer<Validator.ValidationBuilder>> profiles = new HashMap<>();

    /**
     * Registers a validation profile with a given name.
     *
     * @param name the name of the validation profile
     * @param profile a {@code Consumer} that applies validation rules to a {@code ValidationBuilder}
     */
    public static void registerProfile(String name, Consumer<Validator.ValidationBuilder> profile) {
        profiles.put(name, profile);
    }

    /**
     * Retrieves a registered validation profile by name.
     *
     * @param name the name of the validation profile
     * @return the {@code Consumer} associated with the profile, or {@code null} if not found
     */
    public static Consumer<Validator.ValidationBuilder> getProfile(String name) {
        return profiles.get(name);
    }
}