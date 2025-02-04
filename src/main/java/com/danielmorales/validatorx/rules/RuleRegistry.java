package com.danielmorales.validatorx.rules;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class RuleRegistry {
    private static final Map<String, Predicate<Object>> rules = new HashMap<>();

    /**
     * Registers a validation rule with a given name.
     *
     * @param name the name of the rule
     * @param rule the rule predicate to register
     */
    public static void registerRule(String name, Predicate<Object> rule) {
        rules.put(name, rule);
    }

    /**
     * Retrieves a registered validation rule by its name.
     *
     * @param name the name of the rule
     * @return the rule predicate, or null if no rule is registered with that name
     */
    public static Predicate<Object> getRule(String name) {
        return rules.get(name);
    }
}
