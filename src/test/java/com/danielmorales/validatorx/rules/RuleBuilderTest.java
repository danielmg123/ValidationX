package com.danielmorales.validatorx.rules;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class RuleBuilderTest {

    @Test
    void testLengthAtLeast_withDefaultMessage() {
        RuleBuilder builder = new RuleBuilder();
        Predicate<Object> rule = builder.lengthAtLeast(5).build();

        assertFalse(rule.test("1234"), "String of length 4 should fail the rule");
        assertTrue(rule.test("12345"), "String of length 5 should pass the rule");

        // Downcast to the composite rule (for demonstration) and get messages
        String errorMsgs = ((RuleBuilder.CompositeRule) rule).getErrorMessages();
        assertTrue(errorMsgs.contains("Must be at least 5 characters long"),
                "Expected default error message for lengthAtLeast(5)");
    }

    @Test
    void testLengthAtLeast_withCustomMessage() {
        RuleBuilder builder = new RuleBuilder();
        Predicate<Object> rule = builder.lengthAtLeast(3, "String too short!").build();

        assertFalse(rule.test("12"), "String length 2 fails the custom rule");
        assertTrue(rule.test("123"), "String length 3 passes the rule");

        String errorMsgs = ((RuleBuilder.CompositeRule) rule).getErrorMessages();
        assertTrue(errorMsgs.contains("String too short!"), "Should contain custom error message");
    }

    @Test
    void testMatches_withDefaultMessage() {
        RuleBuilder builder = new RuleBuilder();
        Predicate<Object> rule = builder.matches("^\\d+$") // must be digits
                                    .build();

        assertTrue(rule.test("1234"), "Digits only should pass");
        assertFalse(rule.test("abc"), "Non-digits should fail");

        String errorMsgs = ((RuleBuilder.CompositeRule) rule).getErrorMessages();
        assertTrue(errorMsgs.contains("Must match regex: ^\\d+$"),
                "Should contain default regex message");
    }

    @Test
    void testMatches_withCustomMessage() {
        RuleBuilder builder = new RuleBuilder();
        Predicate<Object> rule = builder.matches("^\\w+$", "Must be alphanumeric").build();

        assertTrue(rule.test("abc123"), "Alphanumeric should pass");
        assertFalse(rule.test("abc-123"), "Dash is invalid per regex ^\\w+$");

        String errorMsgs = ((RuleBuilder.CompositeRule) rule).getErrorMessages();
        assertTrue(errorMsgs.contains("Must be alphanumeric"), 
                "Should contain custom regex message");
    }

    @Test
    void testCompositeRule_multipleRules() {
        // Build a rule that checks length >= 5 AND must match digits
        RuleBuilder builder = new RuleBuilder();
        Predicate<Object> rule = builder
                .lengthAtLeast(5, "Not enough digits")
                .matches("^\\d+$", "Must be digits only")
                .build();

        assertFalse(rule.test("1234"), "Fails lengthAtLeast(5)");
        assertFalse(rule.test("12345abc"), "Fails 'digits only' rule");
        assertTrue(rule.test("12345"), "Should pass both rules");

        String errorMsgs = ((RuleBuilder.CompositeRule) rule).getErrorMessages();
        // The composite error message includes both. 
        assertTrue(errorMsgs.contains("Not enough digits"),
                "Composite should contain first message");
        assertTrue(errorMsgs.contains("Must be digits only"), 
                "Composite should contain second message");
    }
}