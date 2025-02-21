# Advanced Topics

This document covers advanced topics for ValidatorX. It is intended for developers who wish to extend the library’s functionality, integrate with other standards, or contribute to its development. Topics include creating custom validation rules, integrating JSR 380 annotations, setting up custom validation pipelines, and contributing new features.

---

## 1. Creating Custom Validation Rules

ValidatorX provides a flexible API for defining your own validation rules. Custom rules can be defined using the **RuleBuilder** and then registered via the **RuleRegistry** for reuse across your application.

### Using RuleBuilder

You can build a custom rule using the fluent API provided by the `RuleBuilder` class. For example, if you want to validate that a string contains only uppercase letters:

```java
import com.danielmorales.validatorx.rules.RuleBuilder;
import com.danielmorales.validatorx.rules.RuleRegistry;

// Create a rule using RuleBuilder
Predicate<Object> uppercaseRule = new RuleBuilder()
    .matches("^[A-Z]+$", "Field must contain only uppercase letters")
    .build();

// Optionally, register the rule for reuse
RuleRegistry.registerRule("uppercaseOnly", uppercaseRule);
```

Later, apply the rule in your validation pipeline:

```java
ValidationResult result = Validator.check(someObject)
    .applyRule("uppercaseOnly", "code", "Code must contain only uppercase letters")
    .validate();

if (result.hasErrors()) {
    result.getErrors().forEach(System.out::println);
}
```

---

## 2. Integrating JSR 380 Annotations

ValidatorX can integrate with standard JSR 380 (Bean Validation) annotations alongside its custom annotations.

### How It Works

- **JSR 380 Integration:**  
  The `Validator.checkWithJsr380()` method in ValidatorX internally calls the JSR 380 validator to check annotations like `@javax.validation.constraints.NotNull`, `@javax.validation.constraints.Email`, etc.

- **Usage Example:**

Define a model that uses both custom and JSR 380 annotations:

```java
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import com.danielmorales.validatorx.annotations.NotNull as CustomNotNull;

public class MixedUser {
    // JSR 380 NotNull
    @NotNull(message = "JSR NotNull: name cannot be null")
    private String name;

    // Custom ValidatorX Email annotation
    @Email(messageKey = "error.invalidEmail")
    private String email;

    // Custom ValidatorX NotNull annotation
    @CustomNotNull(message = "Custom NotNull: password must not be null")
    private String password;

    // Constructors, getters, and setters...
}
```

Then, validate using the combined method:

```java
MixedUser user = new MixedUser(null, "test@example.com", null);
ValidationResult result = Validator.checkWithJsr380(user);

if (result.hasErrors()) {
    result.getErrors().forEach(System.out::println);
}
```

This method merges errors from both JSR 380 and ValidatorX’s own validations.

---

## 3. Setting Up Custom Validation Pipelines

ValidatorX includes a pipeline mechanism to orchestrate complex validation workflows, using custom rule sets and callbacks.

### Custom Pipeline with Rule Sets

You can define custom pipelines by registering rule sets with the `ValidationProfileRegistry` and then executing them using the pipeline API.

#### Step 1: Register a Custom Rule Set

```java
import com.danielmorales.validatorx.core.ValidationProfileRegistry;

// Register a rule set for ensuring a field is not empty
ValidationProfileRegistry.registerProfile("nonEmptyName", builder ->
    builder.isNotNull("name", "Name must not be null!")
           .hasLengthBetween("name", 1, 100, "Name cannot be empty")
);
```

#### Step 2: Create and Execute the Pipeline

```java
import com.danielmorales.validatorx.pipeline.DefaultValidationPipeline;
import com.danielmorales.validatorx.pipeline.ValidationPipeline;

// Assuming 'user' is an instance of your model
ValidationPipeline<User> pipeline = new DefaultValidationPipeline<User>()
    .validateRequest(user)
    .withRuleSet("nonEmptyName")
    .onFailure(result -> {
        // Handle failure (log errors, return response, etc.)
        result.getErrors().forEach(System.out::println);
    })
    .onSuccess(() -> {
        // Proceed with further processing
        System.out.println("Validation succeeded!");
    });

pipeline.execute();
```

This setup allows you to define separate validation profiles and easily switch or combine them as needed.

---

## 4. Contributing New Features

ValidatorX is an open-source project, and contributions are welcome. Here are some guidelines for contributing:

### Areas for Contribution

- **New Validation Rules:**  
  Add new custom annotations or rule builders to handle additional validation scenarios.

- **Enhanced JSR 380 Integration:**  
  Improve the merging of standard JSR 380 validations with ValidatorX's existing validations.

- **Performance Improvements:**  
  Optimize reflection caching or validation algorithms for high-performance environments.

- **Documentation and Examples:**  
  Expand the documentation, provide more real-world examples, and improve API references.

### Contribution Guidelines

1. **Fork the Repository:**  
   Start by forking the ValidatorX repository on GitHub.

2. **Create a Feature Branch:**  
   Create a branch for your feature or bug fix.

3. **Follow the Code Style:**  
   Make sure your code follows the existing style conventions and includes Javadoc comments.

4. **Write Tests:**  
   Add or update tests in the `src/test/java` directory to cover your changes.

5. **Submit a Pull Request:**  
   Once your changes are complete and tested, submit a pull request with a clear description of your contributions.

6. **Review Process:**  
   Your pull request will be reviewed, and you may be asked for additional changes or clarifications.

---

By exploring these advanced topics, you can tailor ValidatorX to your specific needs and help contribute to its growth and improvement.