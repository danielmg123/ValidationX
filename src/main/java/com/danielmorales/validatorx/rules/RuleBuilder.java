package com.danielmorales.validatorx.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RuleBuilder {
    private final List<Predicate<String>> conditions = new ArrayList<>();

    public RuleBuilder lengthAtLeast(int len) {
        conditions.add(str -> str.length() >= len);
        return this;
    }

    public RuleBuilder matches(String regex) {
        conditions.add(str -> str.matches(regex));
        return this;
    }

    // Build into a single Predicate<Object>
    public Predicate<Object> build() {
        return (Object value) -> {
            if (value instanceof String) {
                String str = (String) value;
                for (Predicate<String> cond : conditions) {
                    if (!cond.test(str)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        };
    }
}