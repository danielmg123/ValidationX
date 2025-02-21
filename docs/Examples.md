# Examples

This document provides a collection of real-world examples and sample projects to help you understand how to use, integrate, and extend ValidatorX in your applications. Below are several scenarios demonstrating common validation tasks, integration with frameworks, and how to add custom functionality.

---

## 1. Basic Validation with Annotations

ValidatorX supports annotation-based validations directly on your data models.

### Example: User Model Validation

Define a user model with built-in annotations:

```java
import com.danielmorales.validatorx.annotations.NotNull;
import com.danielmorales.validatorx.annotations.Email;
import com.danielmorales.validatorx.annotations.Size;

public class User {
    @NotNull(message = "Name cannot be null!")
    private String name;

    @Email(messageKey = "error.invalidEmail")
    private String email;

    @Size(min = 8, max = 20, message = "Password must be 8-20 chars")
    private String password;

    // Constructors, getters, and setters...
}
```

Validate the model as follows:

```java
User user = new User("John Doe", "john@example.com", "securePass123");
ValidationResult result = Validator.check(user).validate();

if (result.hasErrors()) {
    result.getErrors().forEach(error -> System.out.println(error));
} else {
    System.out.println("Validation passed!");
}
```

---

## 2. Fluent API Validation

For more dynamic scenarios or when you want to validate data without annotations, you can use the fluent API.

### Example: Fluent Rule-Based Validation

Create a model and validate it using a chain of rules:

```java
public class User {
    private String name;
    private String email;
    private String password;

    // Constructors, getters, and setters...
}

User user = new User();
user.setName(null);           // Invalid: name is null
user.setEmail("invalid-email"); // Invalid: does not contain "@" and "."
user.setPassword("short");     // Invalid: too short

ValidationResult result = Validator.check(user)
    .skipAnnotations() // Skip annotation-based validations
    .isNotNull("name", "Name must not be null")
    .isEmail("email", "Email is invalid")
    .hasLengthBetween("password", 8, 20, "Password must be between 8 and 20 characters")
    .validate();

if (result.hasErrors()) {
    result.getErrors().forEach(error -> System.out.println(error));
} else {
    System.out.println("Validation passed!");
}
```

---

## 3. Integration into Frameworks

ValidatorX can be easily integrated into various frameworks. For example, in a Spring Boot REST controller, you can validate incoming JSON payloads.

### Example: Spring Boot Integration

#### Step 1: Define a DTO with annotations

```java
import com.danielmorales.validatorx.annotations.NotNull;
import com.danielmorales.validatorx.annotations.Email;
import com.danielmorales.validatorx.annotations.Size;

public class UserDTO {
    @NotNull(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 8, max = 20, message = "Password must be 8-20 characters")
    private String password;

    // Getters and setters...
}
```

#### Step 2: Validate in a REST Controller

```java
import com.danielmorales.validatorx.core.ValidationResult;
import com.danielmorales.validatorx.core.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDto) {
        ValidationResult result = Validator.check(userDto).validate();
        if (result.hasErrors()) {
            // Return errors in the response (you can further format the errors as needed)
            return ResponseEntity.badRequest().body(result.getErrors());
        }
        // Proceed with processing the valid user data
        return ResponseEntity.ok("User registered successfully");
    }
}
```

---

## 4. Extending ValidatorX

ValidatorX is designed to be extended with custom rules and localized messages.

### Example: Registering a Custom Rule

You can register a custom validation rule using the `RuleRegistry`.

```java
import com.danielmorales.validatorx.rules.RuleRegistry;

// Define a custom rule: a string must contain only uppercase letters.
RuleRegistry.registerRule("uppercaseOnly", value -> {
    if (value instanceof String) {
        return ((String) value).matches("^[A-Z]+$");
    }
    return false;
});

// Later, use this custom rule in your fluent validations:
ValidationResult result = Validator.check(someObject)
    .applyRule("uppercaseOnly", "code", "Code must contain only uppercase letters")
    .validate();

if (result.hasErrors()) {
    result.getErrors().forEach(error -> System.out.println(error));
}
```

### Example: Customizing Localization

To support multiple languages, add localized resource bundles (e.g., `message_fr.properties`) in your resources directory. Then, switch the locale at runtime:

```java
import com.danielmorales.validatorx.i18n.MessageResolver;
import java.util.Locale;

// Set locale to French
MessageResolver.setLocale(Locale.FRANCE);

// The MessageResolver will now use messages from message_fr.properties, if available.
String errorMsg = MessageResolver.getMessage("error.notNull", "nom");
System.out.println(errorMsg); // Should print the French localized message.
```
---

These examples illustrate how ValidatorX can be applied in various contexts to ensure robust and flexible data validation. Feel free to mix and match techniques to suit your application's requirements.