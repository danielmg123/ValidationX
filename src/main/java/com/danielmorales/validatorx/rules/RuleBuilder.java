package com.danielmorales.validatorx.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RuleBuilder {
    private final List<Rule> rules = new ArrayList<>();

    /**
     * Adds a rule requiring that a string's length is at least the specified value.
     * Uses a default error message.
     * 
     * @param len the minimum length
     * @return the current RuleBuilder instance
     */
    public RuleBuilder lengthAtLeast(int len) {
        return lengthAtLeast(len, "Must be at least " + len + " characters long");
    }

    /**
     * Adds a rule requiring that a string's length is at least the specified value,
     * with a custom error message.
     * 
     * @param len the minimum length
     * @param errorMsg the custom error message to use if the rule fails
     * @return the current RuleBuilder instance
     */
    public RuleBuilder lengthAtLeast(int len, String errorMsg) {
        rules.add(new Rule(str -> str.length() >= len, errorMsg));
        return this;
    }

    /**
     * Adds a rule requiring that a string matches the given regular expression.
     * Uses a default error message.
     * 
     * @param regex the regular expression
     * @return the current RuleBuilder instance
     */
    public RuleBuilder matches(String regex) {
        return matches(regex, "Must match regex: " + regex);
    }

    /**
     * Adds a rule requiring that a string matches the given regular expression,
     * with a custom error message.
     * 
     * @param regex the regular expression
     * @param errorMsg the custom error message to use if the rule fails
     * @return the current RuleBuilder instance
     */
    public RuleBuilder matches(String regex, String errorMsg) {
        rules.add(new Rule(str -> str.matches(regex), errorMsg));
        return this;
    }

    /**
     * Combines all added rules into a single composite rule.
     */
    public Predicate<Object> build() {
        return new CompositeRule(rules);
    }

    /**
     * Represents an individual validation rule that holds a predicate and a custom error message.
     */
    public static class Rule {
        private final Predicate<String> predicate;
        private final String errorMessage;

        public Rule(Predicate<String> predicate, String errorMessage) {
            this.predicate = predicate;
            this.errorMessage = errorMessage;
        }

        public boolean test(String value) {
            return predicate.test(value);
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    /**
     * A composite rule that aggregates multiple rules.
     */
    public static class CompositeRule implements Predicate<Object> {
        private final List<Rule> rules;

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
         * Returns a concatenated string of all custom error messages from the composed rules.
         * 
         * @return a String containing error messages
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
