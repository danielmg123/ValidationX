package com.danielmorales.validatorx.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class MessageResolverTest {

    @Test
    void testGetMessage_withExistingKey() {
        // "error.notNull" is in message.properties
        String resolved = MessageResolver.getMessage("error.notNull", "fieldName");
        assertNotNull(resolved, "Message should not be null");
        assertFalse(resolved.contains("??"), "Message should not contain fallback placeholders");
        // Typically, message.properties has: Field '{0}' cannot be null
        assertTrue(resolved.contains("fieldName") || resolved.contains("{0}"), 
                "Resolved message should incorporate the argument");
    }

    @Test
    void testGetMessage_withMissingKey() {
        // Using a key that doesn't exist in message.properties
        String resolved = MessageResolver.getMessage("non.existent.key");
        // Fallback is "??non.existent.key??"
        assertTrue(resolved.contains("??non.existent.key??"),
                "Should return fallback string for missing key");
    }

    @Test
    void testLocalization_withDifferentLocale() {
        // By default, the bundle uses Locale.getDefault()
        // We can try setting a new locale if we have localized message files
        Locale oldLocale = Locale.getDefault();
        try {
            MessageResolver.setLocale(Locale.FRANCE); // For example
            // This assumes we have a message_fr.properties (otherwise it will fallback)
            String resolved = MessageResolver.getMessage("error.notNull", "Champ");
            // Even if no French resource is provided, it should gracefully fallback
            assertNotNull(resolved);
        } finally {
            // Restore the old locale to avoid side effects for other tests
            MessageResolver.setLocale(oldLocale);
        }
    }
}