package com.danielmorales.validatorx.rules;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * A global registry of named validation rules.
 * Example usage:
 *    RuleRegistry.registerRule("strongPassword", value -> { ... });
 *    // Then in fluent builder: .applyRule("strongPassword", "password", "Weak password!")
 */
public class RuleRegistry {
    private static final Map<String, Predicate<Object>> rules = new HashMap<>();

    public static void registerRule(String name, Predicate<Object> rule) {
        rules.put(name, rule);
    }

    public static Predicate<Object> getRule(String name) {
        return rules.get(name);
    }
}