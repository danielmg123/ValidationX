package com.danielmorales.validatorx.rules;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class RuleRegistryTest {

    @AfterEach
    void cleanup() {
        // Not strictly necessary unless we want to clear the global registry 
        // between tests.
    }

    @Test
    void testRegisterAndGetRule() {
        Predicate<Object> isString = obj -> obj instanceof String;
        RuleRegistry.registerRule("isString", isString);

        Predicate<Object> retrieved = RuleRegistry.getRule("isString");
        assertNotNull(retrieved, "Should retrieve the previously registered rule");
        assertTrue(retrieved.test("hello"), "Should pass for string");
        assertFalse(retrieved.test(123), "Should fail for non-string");
    }

    @Test
    void testGetRule_nonExistent() {
        Predicate<Object> rule = RuleRegistry.getRule("noSuchRule");
        assertNull(rule, "Should return null if rule doesn't exist");
    }

    @Test
    void testOverwritingRule() {
        // If we register the same name again, we effectively replace the old rule
        Predicate<Object> originalRule = obj -> ((String)obj).length() > 3;
        Predicate<Object> newRule = obj -> ((String)obj).length() > 5;

        RuleRegistry.registerRule("lengthRule", originalRule);
        assertNotNull(RuleRegistry.getRule("lengthRule"), 
                "Should retrieve the original rule");
        assertTrue(RuleRegistry.getRule("lengthRule").test("abcd"), 
                "Original rule expects length > 3 (so 'abcd' is length=4 => pass)");

        // Overwrite
        RuleRegistry.registerRule("lengthRule", newRule);
        assertNotNull(RuleRegistry.getRule("lengthRule"), 
                "Should retrieve the new rule after overwrite");
        assertFalse(RuleRegistry.getRule("lengthRule").test("abcd"), 
                "New rule expects length > 5 => fails for 'abcd'");
    }
}