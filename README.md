# ValidatorX

ValidatorX is a flexible Java library for data validation designed for both simple forms and complex enterprise applications. It provides a fluent API for building validations, supports annotation-based rules, and integrates with JSR 380 for standard validation constraints.

## Features

- **Fluent API:** Easily chain validation rules for clear, readable code.
- **Annotation-Based Validation:** Use custom annotations such as `@NotNull`, `@Email`, `@Size`, `@Min`, and `@Max` to enforce constraints.
- **Custom Rules:** Define and register custom validation rules with the `RuleBuilder` and `RuleRegistry`.
- **Cascading Validation:** Automatically validate nested objects.
- **JSR 380 Integration:** Combine ValidatorX validations with standard JSR 380 annotations.
- **Localization Support:** Customize error messages through resource bundles.

## Getting Started

### Installation

Add ValidatorX to your Maven project by including the following dependency in your `pom.xml`:

```xml
<dependency>
    <groupId>com.danielmorales.validatorx</groupId>
    <artifactId>validatorx</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Basic Usage

Here’s a quick example of how to use ValidatorX:

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

ValidatorX allows you to define validations in a fluent manner:

```java
ValidationResult result = Validator.check(user)
    .isNotNull("name", "Name is required")
    .isEmail("email", "Please provide a valid email address")
    .hasLengthBetween("password", 8, 20, "Password must be between 8 and 20 characters")
    .validate();
```

## Documentation

For detailed documentation including API reference, configuration options, and advanced topics such as creating custom validation rules and integrating JSR 380, please see our [Documentation](docs/Introduction.md).

## License

ValidatorX is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for more details.

---