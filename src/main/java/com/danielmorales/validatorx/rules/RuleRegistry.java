package com.danielmorales.validatorx.rules;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * A registry for managing reusable validation rules.
 * <p>
 * This class allows registering and retrieving named validation rules
 * that can be applied dynamically in validation workflows.
 */
public class RuleRegistry {
    private static final Map<String, Predicate<Object>> rules = new HashMap<>();


    /**
     * Registers a validation rule with a given name.
     * <p>
     * The rule can be retrieved later using {@link #getRule(String)}.
     *
     * @param name the name of the rule.
     * @param rule the validation logic as a {@code Predicate<Object>}.
     */
    public static void registerRule(String name, Predicate<Object> rule) {
        rules.put(name, rule);
    }

    /**
     * Retrieves a registered validation rule by its name.
     * <p>
     * If no rule is found, this method returns {@code null}.
     *
     * @param name the name of the rule.
     * @return the rule predicate, or {@code null} if no rule is registered under the given name.
     */
    public static Predicate<Object> getRule(String name) {
        return rules.get(name);
    }
}
