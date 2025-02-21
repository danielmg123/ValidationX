# Configuration

ValidatorX can be customized through configuration files and localized messages. This document explains the available properties, how to modify them, and how to customize messages and localization in your project.

---

## 1. Configuration Files

ValidatorX uses two primary resource files:

### config.properties

- **Purpose:**  
  This file may be used to define various configuration settings for your application. (Currently, it serves as a placeholder for future configuration options such as logging levels, performance settings, or integration options.)

- **Location:**  
  `src/main/resources/config.properties`

- **Usage:**  
  If you need to add custom settings for your validation or related functionality, you can define key-value pairs in this file. ValidatorX does not process these settings by default, but you can extend the library or your application to read and use these values.

### message.properties

- **Purpose:**  
  This file contains default error messages for validation failures. Each message is keyed so that it can be easily referenced by the annotations and the validation engine.

- **Location:**  
  `src/main/resources/message.properties`

- **Default Entries:**

```
error.notNull=Field '{0}' cannot be null
error.invalidEmail=Field '{0}' must be a valid email
error.size=Field '{0}' must have valid size
error.min=Field '{0}' must be >= {1}
error.max=Field '{0}' must be <= {1}
error.pattern=Field '{0}' must match the pattern
```

- **Usage in ValidatorX:**  
  When a validation fails (for example, when a field annotated with `@NotNull` is null), the `MessageResolver` is used to look up the corresponding message by its key. The error message may also include parameters (such as the field name or a numeric value) using a simple placeholder format (e.g., `{0}`, `{1}`).

---

## 2. Customizing Messages and Localization

### Overriding Default Messages

You can override any default message by editing the `message.properties` file. For example, to change the error message for a null field:

```properties
error.notNull=The field '{0}' is required and cannot be empty.
```

### Using Custom Messages in Annotations

While using ValidatorX annotations, you have two options to specify an error message:

1. **Direct Custom Message:**  
   Specify a custom message directly in the annotation. For example:

   ```java
   @NotNull(message = "Name must be provided")
   private String name;
   ```

2. **Custom Message Key:**  
   Instead of providing a message, you can specify a message key that corresponds to an entry in `message.properties`:

   ```java
   @Email(messageKey = "error.invalidEmail")
   private String email;
   ```

   In this case, the `MessageResolver` will look up the key `error.invalidEmail` in the resource file and use that message.

### Localization with MessageResolver

ValidatorX supports localization via the `MessageResolver` class. By default, it loads messages based on the default locale:

- **Default Behavior:**  
  The `MessageResolver` loads the resource bundle using:
  ```java
  ResourceBundle.getBundle("message", Locale.getDefault())
  ```
  This means that if your system locale is set to, say, US English, it will load the default `message.properties`.

- **Changing the Locale:**  
  To support multiple languages, you can create additional resource bundles (for example, `message_fr.properties` for French). To change the locale at runtime, call:
  ```java
  MessageResolver.setLocale(Locale.FRANCE);
  ```
  This reloads the resource bundle for the new locale. Once set, all subsequent message lookups will use the localized messages.

- **Example:**  
  Suppose you have a French resource file with the following entry:
  ```properties
  error.notNull=Le champ '{0}' ne peut pas être nul
  ```
  After setting the locale:
  ```java
  MessageResolver.setLocale(Locale.FRANCE);
  String message = MessageResolver.getMessage("error.notNull", "nom");
  // message will be: "Le champ 'nom' ne peut pas être nul"
  ```

---

## 3. Extending Configuration

If you wish to introduce new configurable properties (for example, to control caching behavior or logging), you can:

1. **Add New Entries:**  
   Define new keys in `config.properties`.

2. **Implement a Reader:**  
   Create or extend a configuration utility in your project that reads `config.properties` (for example, using `java.util.Properties`).

3. **Integrate with ValidatorX:**  
   Use these properties to conditionally control behavior in your extended version of ValidatorX or in your application logic.

---

By editing the provided resource files and using the built-in localization support, you can fully customize the behavior and presentation of validation messages in your project.