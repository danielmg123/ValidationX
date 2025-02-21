# ValidatorX API Reference

This document provides detailed descriptions of the public classes, interfaces, and annotations in ValidatorX. Each section includes method summaries, parameter details, and usage examples.

---

## 1. Core Classes

### Validator

The central class for performing validations in ValidatorX.

#### Key Methods

- **`static ValidationBuilder check(Object target)`**  
  *Entry point for creating a validation builder.*  
  **Parameters:**
    - `target`: The object to be validated.  
      **Returns:**
    - A new instance of `Validator.ValidationBuilder`.

- **`static ValidationResult checkWithJsr380(Object target)`**  
  *Performs both ValidatorX validations and JSR 380 validations.*  
  **Parameters:**
    - `target`: The object to validate.  
      **Returns:**
    - A merged `ValidationResult` containing errors from both mechanisms.

#### Inner Class: ValidationBuilder

Provides a fluent API to build and execute validation rules.

**Notable Methods:**

- **`skipAnnotations()`**  
  Disables annotation-based validations.  
  **Returns:**
    - The current `ValidationBuilder` instance.

- **`isNotNull(String fieldName, String customMsg)`**  
  Validates that a field is not null.  
  **Parameters:**
    - `fieldName`: Name of the field.
    - `customMsg`: Custom error message if validation fails.

- **`isEmail(String fieldName, String customMsg)`**  
  Checks that a field contains a valid email address.

- **`hasLengthBetween(String fieldName, int min, int max, String customMsg)`**  
  Ensures a string’s length is within the specified range.

- **`matchesRegex(String fieldName, String regex, String customMsg)`**  
  Validates that a string field matches a regular expression.

- **`cascade(String fieldName)`**  
  Performs recursive validations on nested objects or collections.

- **`validate()`**  
  Executes the built validations and returns a `ValidationResult`.

- **`validateAndThrow()`**  
  Executes validations and throws a `ValidationException` if errors are found.

**Usage Example:**

```java
ValidationResult result = Validator.check(user)
    .skipAnnotations()
    .isNotNull("name", "Name must not be null")
    .isEmail("email", "Invalid email format")
    .hasLengthBetween("password", 8, 20, "Password must be between 8 and 20 characters")
    .validate();
```

### ValidationResult

Represents the result of the validation process.

#### Key Methods

- **`void addError(ValidationError error)`**  
  Adds an error to the result.

- **`List<ValidationError> getErrors()`**  
  Retrieves the list of validation errors.

- **`boolean hasErrors()`**  
  Checks whether there are any validation errors.

### ValidationError

Encapsulates a single validation error.

#### Attributes

- **`fieldName`**: The name of the field that failed validation.
- **`message`**: The error message.
- **`invalidValue`**: The invalid value that triggered the error.

#### Methods

- **`String getFieldName()`**
- **`String getMessage()`**
- **`Object getInvalidValue()`**
- **`String toString()`**  
  Returns a formatted string representation of the error.

### ValidationException

A runtime exception thrown when validation fails.

#### Constructor

- **`ValidationException(String message, ValidationResult result)`**  
  **Parameters:**
    - `message`: The exception message.
    - `result`: The associated `ValidationResult` containing errors.

#### Method

- **`ValidationResult getValidationResult()`**  
  Returns the validation result stored in the exception.

### ValidatorEngine

Handles annotation-based validations by scanning an object’s fields.

#### Key Methods

- **`ValidationResult accumulateValidate(Object target)`**  
  Accumulates errors from all annotation validations, including cascading validations for nested objects.

- **`ValidationResult validateAndThrow(Object target)`**  
  Performs validations and throws a `ValidationException` if any errors exist.

---

## 2. Annotations

ValidatorX provides a set of custom annotations for common validation tasks.

### @NotNull

Ensures that a field is not null.

**Attributes:**

- **`messageKey()`**: Default is `"error.notNull"`.
- **`message()`**: Custom error message (optional).

**Usage Example:**

```java
@NotNull(message = "Name cannot be null!")
private String name;
```

---

### @Email

Validates that a field contains a properly formatted email address.

**Attributes:**

- **`messageKey()`**: Default is `"error.invalidEmail"`.
- **`message()`**: Custom error message.
- **`regex()`**: Optional custom regex for strict validation.

**Usage Example:**

```java
@Email(messageKey = "error.invalidEmail")
private String email;
```

---

### @Size

Ensures that a string or collection's size is within a specified range.

**Attributes:**

- **`min()`**: Minimum allowed size (default: 0).
- **`max()`**: Maximum allowed size (default: `Integer.MAX_VALUE`).
- **`messageKey()`**: Default is `"error.size"`.
- **`message()`**: Custom error message.

**Usage Example:**

```java
@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
private String password;
```

---

### @Min

Specifies the minimum numeric value allowed for a field.

**Attributes:**

- **`value()`**: The minimum value.
- **`messageKey()`**: Default is `"error.min"`.
- **`message()`**: Custom error message.

**Usage Example:**

```java
@Min(10)
private int quantity;
```

---

### @Max

Specifies the maximum numeric value allowed for a field.

**Attributes:**

- **`value()`**: The maximum value.
- **`messageKey()`**: Default is `"error.max"`.
- **`message()`**: Custom error message.

**Usage Example:**

```java
@Max(100)
private double price;
```

---

### @Pattern

Ensures that a string field matches a specified regular expression.

**Attributes:**

- **`regex()`**: The regular expression pattern.
- **`messageKey()`**: Default is `"error.pattern"`.
- **`message()`**: Custom error message.

**Usage Example:**

```java
@Pattern(regex = "^[a-zA-Z0-9]{5,10}$", message = "Username must be 5-10 alphanumeric characters")
private String username;
```

---

## 3. Pipeline Components

ValidatorX includes a validation pipeline for orchestrating complex validation workflows.

### ValidationPipeline (Interface)

Defines a fluent API for handling validations with rule sets and callbacks.

**Key Methods:**

- **`validateRequest(T request)`**: Specifies the object to validate.
- **`withRuleSet(String ruleSetName)`**: Associates a registered rule set with the validation.
- **`onFailure(Consumer<ValidationResult> action)`**: Callback executed when validations fail.
- **`onSuccess(Runnable action)`**: Callback executed when validations pass.
- **`execute()`**: Runs the validation pipeline.

### DefaultValidationPipeline

A default implementation of the `ValidationPipeline` interface.

**Usage Example:**

```java
ValidationPipeline<User> pipeline = new DefaultValidationPipeline<User>()
    .validateRequest(user)
    .withRuleSet("nonEmptyName")
    .onFailure(result -> {
        // Handle validation failures (e.g., log errors, show messages)
    })
    .onSuccess(() -> {
        // Proceed with business logic on success
    });

pipeline.execute();
```

---

## 4. Rules

ValidatorX provides utilities for defining custom validation rules.

### RuleBuilder

A fluent builder for creating composite validation rules.

**Key Methods:**

- **`lengthAtLeast(int len)`**: Adds a rule that requires a string to have at least a certain length.
- **`matches(String regex)`**: Adds a rule that requires a string to match a regular expression.
- **`build()`**: Combines added rules into a composite `Predicate<Object>`.

**Usage Example:**

```java
Predicate<Object> rule = new RuleBuilder()
    .lengthAtLeast(5, "Not enough characters")
    .matches("^\\d+$", "Must be digits only")
    .build();

if (!rule.test("1234")) {
    System.out.println("Validation failed: " +
        ((RuleBuilder.CompositeRule) rule).getErrorMessages());
}
```

### RuleRegistry

A registry to store and retrieve named custom rules.

**Key Methods:**

- **`static void registerRule(String name, Predicate<Object> rule)`**  
  Registers a custom rule.
- **`static Predicate<Object> getRule(String name)`**  
  Retrieves a rule by name.

---

This API Reference offers an in-depth look at ValidatorX’s public API. For further details, advanced use cases, and additional examples, please refer to the source code and other documentation files.