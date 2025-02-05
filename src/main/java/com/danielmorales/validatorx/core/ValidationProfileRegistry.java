package com.danielmorales.validatorx.core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A registry for grouping additional validations into named profiles (or rule sets).
 * Profiles are defined as Consumers that accept a ValidationBuilder.
 */
public class ValidationProfileRegistry {
    private static final Map<String, Consumer<Validator.ValidationBuilder>> profiles = new HashMap<>();

    public static void registerProfile(String name, Consumer<Validator.ValidationBuilder> profile) {
        profiles.put(name, profile);
    }

    public static Consumer<Validator.ValidationBuilder> getProfile(String name) {
        return profiles.get(name);
    }
}