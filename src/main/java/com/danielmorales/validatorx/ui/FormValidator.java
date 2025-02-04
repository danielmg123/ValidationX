package com.danielmorales.validatorx.ui;

import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.core.Validator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import java.util.function.Consumer;

public class FormValidator {

    public static UIFieldValidator attach(TextField textField) {
        return new UIFieldValidator(textField);
    }

    public static class UIFieldValidator {
        private final TextField textField;
        private Consumer<Void> onValidCallback;
        private Consumer<Void> onInvalidCallback;

        // Flag to optionally use annotation-based validation (if applicable)
        private boolean useAnnotations = false;
        private final ValidationResult result = new ValidationResult();

        // Use a custom wrapper object for dynamic value extraction
        private final FieldValueHolder fieldValueHolder = new FieldValueHolder();

        // Build the fluent validator using the FieldValueHolder as target
        private final Validator.ValidationBuilder builder = Validator.check(fieldValueHolder).skipAnnotations();

        // Timeline for debouncing the text-change events
        private Timeline debounceTimeline;

        private UIFieldValidator(TextField textField) {
            this.textField = textField;
            initListener();
        }

        /**
         * Optionally enable annotation-based validation if needed.
         */
        public UIFieldValidator withAnnotations(boolean useAnnotations) {
            this.useAnnotations = useAnnotations;
            return this;
        }

        /**
         * Sets up a debounced listener for text changes.
         */
        private void initListener() {
            textField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    // Cancel any previously scheduled validation
                    if (debounceTimeline != null) {
                        debounceTimeline.stop();
                    }
                    // Schedule validation after a 300ms delay
                    debounceTimeline = new Timeline(new KeyFrame(Duration.millis(300), event -> {
                        validateInput(newValue);
                    }));
                    debounceTimeline.setCycleCount(1);
                    debounceTimeline.play();
                }
            });
        }

        // Example rule: validate that the input is a valid email.
        public UIFieldValidator isEmail(String customMsg) {
            builder.isEmail("value", customMsg);
            return this;
        }

        // Example rule: validate that the input is not null or empty.
        public UIFieldValidator isNotEmpty(String customMsg) {
            builder.isNotNull("value", customMsg);
            return this;
        }

        // Additional rules (e.g., matchesRegex, etc.) can be added here.

        /**
         * Called after the debounce delay to perform validation.
         */
        private void validateInput(String newValue) {
            // Update the wrapper with the new value
            fieldValueHolder.setValue(newValue);

            // Clear previous errors
            result.getErrors().clear();

            // Perform validation using the fluent builder
            ValidationResult checkResult = builder.validate();

            if (checkResult.hasErrors()) {
                result.getErrors().addAll(checkResult.getErrors());
                if (onInvalidCallback != null) {
                    onInvalidCallback.accept(null);
                }
            } else {
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