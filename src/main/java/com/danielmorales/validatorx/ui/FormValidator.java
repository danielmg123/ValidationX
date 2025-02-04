package com.danielmorales.validatorx.ui;

import com.danielmorales.validatorx.core.ValidationError;
import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.core.Validator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

/**
 * Provides real-time validation for JavaFX TextFields.
 * Usage:
 *   FormValidator.attach(emailField)
 *       .isEmail("Invalid email!")
 *       .onValid(() -> emailField.setStyle("-fx-border-color: green;"))
 *       .onInvalid(() -> emailField.setStyle("-fx-border-color: red;"));
 */
public class FormValidator {

    public static UIFieldValidator attach(TextField textField) {
        return new UIFieldValidator(textField);
    }

    public static class UIFieldValidator {
        private final TextField textField;
        private Consumer<Void> onValidCallback;
        private Consumer<Void> onInvalidCallback;
        
        private boolean useAnnotations = false;
        private final ValidationResult result = new ValidationResult();

        // Programmatic checks
        // Example: .isEmail("Error message")
        private final Validator.ValidationBuilder builder;

        private UIFieldValidator(TextField textField) {
            this.textField = textField;
            this.builder = Validator.check(textField) // we'll store text in a "value" field below
                                .skipAnnotations();   // skip because there's no annotation on a TextField
            initListener();
        }

        /**
         * If we use annotation-based validation on an object that
         * syncs with this TextField, could change logic here,
         */
        public UIFieldValidator withAnnotations(boolean useAnnotations) {
            this.useAnnotations = useAnnotations;
            return this;
        }

        /**
         * This method sets up a listener for text changes and triggers validation.
         */
        private void initListener() {
            textField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    validateInput(newValue);
                }
            });
        }

        // Example rule: Email
        public UIFieldValidator isEmail(String customMsg) {
            // We'll store the field value in a pseudo "value" property
            builder.isEmail("value", customMsg);
            return this;
        }

        // Example rule: NotNull or not empty
        public UIFieldValidator isNotEmpty(String customMsg) {
            builder.isNotNull("value", customMsg);
            return this;
        }

        // can add more rules: matchesRegex, minLength, maxLength, etc.

        /**
         * Called every time the text changes.
         */
        private void validateInput(String newValue) {
            // We trick the builder to think there's a "value" field on 'textField' object
            // but actually, let's just store newValue in a simple field and re-validate:
            textField.getProperties().put("value", newValue);

            // Clear previous errors
            result.getErrors().clear();

            // Actually do the validation
            ValidationResult checkResult = builder.validate();

            if (checkResult.hasErrors()) {
                // Transfer errors to main result
                result.getErrors().addAll(checkResult.getErrors());
                // Trigger onInvalid callback if present
                if (onInvalidCallback != null) {
                    onInvalidCallback.accept(null);
                }
            } else {
                // Valid
                if (onValidCallback != null) {
                    onValidCallback.accept(null);
                }
            }
        }

        public UIFieldValidator onValid(Runnable action) {
            this.onValidCallback = v -> action.run();
            return this;
        }

        public UIFieldValidator onInvalid(Runnable action) {
            this.onInvalidCallback = v -> action.run();
            return this;
        }

        public ValidationResult getValidationResult() {
            return result;
        }
    }
}
