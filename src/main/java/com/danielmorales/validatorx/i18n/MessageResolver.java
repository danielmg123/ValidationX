package com.danielmorales.validatorx.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Utility class for resolving localized messages from a resource bundle.
 * This class provides support for retrieving messages based on locale settings
 * and formatting messages with parameters.
 */
public class MessageResolver {
    // Use "message" to match resource file: message.properties
    private static ResourceBundle bundle = ResourceBundle.getBundle("message", Locale.getDefault());

    /**
     * Sets the locale for message resolution.
     * This updates the resource bundle to use the specified locale.
     *
     * @param locale the new locale to be used for message resolution
     */
    public static void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("message", locale);
    }

    /**
     * Retrieves a localized message for the given key, optionally formatting it with provided arguments.
     *
     * @param key  the message key to look up in the resource bundle
     * @param args optional arguments to format the message
     * @return the resolved and formatted message, or a fallback message if the key is not found
     */
    public static String getMessage(String key, Object... args) {
        try {
            String message = bundle.getString(key);
            if (args != null && args.length > 0) {
                // Simple placeholder replacement using String.format.
                return String.format(message, args);
            }
            return message;
        } catch (MissingResourceException e) {
            // Fallback message if the key is missing
            return "??" + key + "??";
        }
    }
}
