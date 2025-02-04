package com.danielmorales.validatorx.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageResolver {
    private static ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    public static void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("messages", locale);
    }

    public static String getMessage(String key, Object... args) {
        String message = bundle.getString(key);
        if (args != null && args.length > 0) {
            // simple placeholder replacement
            return String.format(message, args);
        }
        return message;
    }
}
