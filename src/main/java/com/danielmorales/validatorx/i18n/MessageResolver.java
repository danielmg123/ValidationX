package com.danielmorales.validatorx.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageResolver {
    // Use "message" to match resource file: message.properties
    private static ResourceBundle bundle = ResourceBundle.getBundle("message", Locale.getDefault());

    public static void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("message", locale);
    }

    public static String getMessage(String key, Object... args) {
        try {
            String message = bundle.getString(key);
            if (args != null && args.length > 0) {
                // Simple placeholder replacement using String.format.
                // Might support more complex parameterization.
                return String.format(message, args);
            }
            return message;
        } catch (MissingResourceException e) {
            // Fallback message if the key is missing
            return "??" + key + "??";
        }
    }
}
