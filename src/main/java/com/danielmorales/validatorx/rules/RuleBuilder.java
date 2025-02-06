package com.danielmorales.validatorx.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A builder for defining custom validation rules.
 * <p>
 * This class allows constructing a set of validation rules using a fluent API.
 * The resulting rules can be combined into a composite validation logic.
 */
public class RuleBuilder {
    private final List<Rule> rules = new ArrayList<>();

    /**
     * Adds a rule that requires a string to have at least the specified length.
     *
     * @param len the minimum required length.
     * @return the current instance of {@code RuleBuilder}.
     */
    public RuleBuilder lengthAtLeast(int len) {
        return lengthAtLeast(len, "Must be at least " + len + " characters long");
    }

    /**
     * Adds a rule that requires a string to have at least the specified length,
     * using a custom error message.
     *
     * @param len      the minimum required length.
     * @param errorMsg the custom error message for this rule.
     * @return the current instance of {@code RuleBuilder}.
     */
    public RuleBuilder lengthAtLeast(int len, String errorMsg) {
        rules.add(new Rule(str -> str.length() >= len, errorMsg));
        return this;
    }

    /**
     * Adds a rule that requires a string to match the given regular expression.
     *
     * @param regex the regular expression to match.
     * @return the current instance of {@code RuleBuilder}.
     */
    public RuleBuilder matches(String regex) {
        return matches(regex, "Must match regex: " + regex);
    }

    /**
     * Adds a rule that requires a string to match the given regular expression,
     * using a custom error message.
     *
     * @param regex    the regular expression to match.
     * @param errorMsg the custom error message for this rule.
     * @return the current instance of {@code RuleBuilder}.
     */
    public RuleBuilder matches(String regex, String errorMsg) {
        rules.add(new Rule(str -> str.matches(regex), errorMsg));
        return this;
    }

    /**
     * Combines all added rules into a single composite rule.
     *
     * @return a {@code Predicate<Object>} that represents the combined rules.
     */
    public Predicate<Object> build() {
        return new CompositeRule(rules);
    }

    /**
     * Represents an individual validation rule consisting of a predicate and an error message.
     */
    public static class Rule {
        private final Predicate<String> predicate;
        private final String errorMessage;

        /**
         * Creates a validation rule.
         *
         * @param predicate    the condition that must be met for the rule to pass.
         * @param errorMessage the error message if the rule fails.
         */
        public Rule(Predicate<String> predicate, String errorMessage) {
            this.predicate = predicate;
            this.errorMessage = errorMessage;
        }

        /**
         * Tests the given string against the rule.
         *
         * @param value the value to test.
         * @return {@code true} if the value passes the rule, otherwise {@code false}.
         */
        public boolean test(String value) {
            return predicate.test(value);
        }

        /**
         * Gets the error message for this rule.
         *
         * @return the error message.
         */
        public String getErrorMessage() {
            return errorMessage;
        }
    }

    /**
     * A composite rule that aggregates multiple rules.
     */
    public static class CompositeRule implements Predicate<Object> {
        private final List<Rule> rules;

        /**
         * Creates a composite rule from a list of individual rules.
         *
         * @param rules the list of rules to combine.
         */
        public CompositeRule(List<Rule> rules) {
            this.rules = rules;
        }

        @Override
        public boolean test(Object value) {
            if (value instanceof String) {
                String str = (String) value;
                for (Rule rule : rules) {
                    if (!rule.test(str)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        /**
         * Returns a concatenated string of all error messages from failed rules.
         *
         * @return a string containing all error messages.
         */
        public String getErrorMessages() {
            StringBuilder sb = new StringBuilder();
            for (Rule rule : rules) {
                sb.append(rule.getErrorMessage()).append("; ");
            }
            return sb.toString();
        }
    }
}
