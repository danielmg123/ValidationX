# Contributing to ValidatorX

Thank you for your interest in contributing to ValidatorX! We welcome contributions that improve the library, add new features, fix bugs, or enhance documentation. This document outlines the guidelines for contributing to the project.

---

## Table of Contents

1. [Reporting Issues](#reporting-issues)
2. [Submitting Pull Requests](#submitting-pull-requests)
3. [Code Style Guidelines](#code-style-guidelines)
4. [Testing](#testing)
5. [Documentation](#documentation)
6. [Getting Started](#getting-started)
7. [Questions and Support](#questions-and-support)

---

## Reporting Issues

- **Bug Reports:**  
  If you find a bug, please open an issue on the [GitHub Issues page](https://github.com/danielmg123/ValidationX/issues). Provide a clear description, steps to reproduce the issue, and any relevant code snippets or error messages.

- **Feature Requests:**  
  If you have ideas for new features or improvements, please submit a feature request. Describe your idea, how it would benefit the project, and any potential implementation details if possible.

---

## Submitting Pull Requests

Before you begin, please read the following guidelines:

1. **Fork the Repository:**  
   Start by forking the ValidatorX repository on GitHub.

2. **Create a Feature Branch:**  
   Create a new branch from `main` (or the appropriate base branch) for your changes. Use descriptive branch names (e.g., `feature/custom-rule` or `bugfix/null-pointer-error`).

3. **Make Changes and Commit:**  
   - Ensure your changes adhere to our code style guidelines.
   - Write clear, descriptive commit messages.
   - Include Javadoc comments for any new public methods or classes.
   - If you add new functionality, include tests and update existing tests as needed.

4. **Run the Tests:**  
   Make sure all tests pass by running:
   ```bash
   mvn test
   ```
If you introduce new features or bug fixes, add corresponding tests in the `src/test/java` directory.

5. **Submit a Pull Request:**  
   Once your changes are ready, submit a pull request to the main repository. Include a detailed description of your changes, the problem being solved, and any additional context that might help reviewers.

6. **Review Process:**  
   Your pull request will be reviewed by the maintainers. Please be responsive to feedback and be prepared to make adjustments as needed.

---

## Code Style Guidelines

- **Language and Formatting:**
    - Follow standard Java coding conventions (naming conventions, indentation, etc...).
    - Use Java 11 features as the project is compiled with Maven using source/target set to 11.
    - Write clean, readable, and well-documented code. All public classes and methods should have Javadoc comments.

- **Structure:**
    - The project follows a Maven structure. Place new code in the appropriate package under `src/main/java`.
    - Corresponding tests should be added in `src/test/java` following the package structure.

- **Commit Messages:**
    - Use clear and descriptive commit messages.
    - Follow the format: `[<Component>] Short description` (e.g., `[Validator] Fix null pointer exception in validate()`).

---

## Testing

- **Automated Testing:**  
  ValidatorX uses [JUnit 5 (Jupiter)](https://junit.org/junit5/) for testing. All new features or bug fixes should include appropriate test cases.

- **Running Tests:**  
  Run tests locally using Maven:
  ```bash
  mvn test
  ```
  Ensure that your changes do not break any existing tests.

- **Test Coverage:**  
  Please try to maintain or increase the test coverage when contributing new features.

---

## Documentation

- **Updating Documentation:**  
  If you add new features or modify existing functionality, please update the corresponding documentation files (e.g., README.md, AdvancedTopics.md, etc.).

- **Markdown Files:**  
  This repository includes several Markdown files (e.g., README.md, Examples.md, AdvancedTopics.md) that provide guidance on using and extending ValidatorX. Make sure your contributions are documented if necessary.

---

## Getting Started

- **Clone the Repository:**
  ```bash
  git clone https://github.com/danielmg123/ValidationX.git
  ```
- **Build the Project:**  
  Use Maven to build:
  ```bash
  mvn clean install
  ```
- **Explore the Code:**  
  Familiarize yourself with the project structure, especially:
    - `src/main/java` for source code.
    - `src/test/java` for tests.
    - `src/main/resources` for configuration and message properties.

---

## Questions and Support

If you have any questions or need help, please feel free to contact me directly via GitHub.

Thank you for helping to improve ValidatorX!