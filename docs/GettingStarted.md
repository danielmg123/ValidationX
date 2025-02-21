# Getting Started with ValidatorX

ValidatorX is designed to make data validation in Java simple and intuitive. Follow these steps to set up ValidatorX in your project and start validating your data.

## 1. Installation

ValidatorX is available as a Maven dependency. To include it in your project, add the following dependency to your `pom.xml` file:

```xml
<dependency>
    <groupId>com.danielmorales.validatorx</groupId>
    <artifactId>validatorx</artifactId>
    <version>1.0.0</version>
</dependency>
```

Ensure that your project is set to use Java 11 (or later) by configuring the Maven compiler plugin:

```xml
<properties>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
</properties>
```

## 2. Configuration

ValidatorX uses resource bundles for error messages. You can customize the messages in the file located at `src/main/resources/message.properties`. For example:

```
error.notNull=Field '{0}' cannot be null
error.invalidEmail=Field '{0}' must be a valid email
error.size=Field '{0}' must have valid size
error.min=Field '{0}' must be >= {1}
error.max=Field '{0}' must be <= {1}
error.pattern=Field '{0}' must match the pattern
```

If you need additional configurations (e.g., logging or other properties), you can also modify the `config.properties` file.

## 3. Quick-Start Guide

ValidatorX supports two primary validation approaches:
- **Annotation-Based Validation:** Use custom annotations like `@NotNull`, `@Email`, and `@Size` directly on your model.
- **Fluent API Validation:** Chain validation rules programmatically.

### Annotation-Based Example

Define your model with annotations:

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

    // Constructors, getters, and setters
}
```

Validate the model:

```java
User user = new User("John Doe", "john@example.com", "securePass123");
ValidationResult result = Validator.check(user).validate();

if (result.hasErrors()) {
    result.getErrors().forEach(error -> System.out.println(error));
} else {
    System.out.println("Validation passed!");
}
```

### Fluent API Example

Perform validations without annotations using the fluent builder:

```java
User user = new User();
user.setName(null);  // Invalid: name is null
user.setEmail("invalid-email");
user.setPassword("short");

ValidationResult result = Validator.check(user)
    .skipAnnotations() // Skip annotation-based checks
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

## 4. Next Steps

For detailed API documentation and advanced topics (such as integrating JSR 380 validations, creating custom rule sets, and cascading validation), please refer to the [API Reference](APIReference.md).