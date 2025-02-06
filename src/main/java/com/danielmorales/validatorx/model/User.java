package com.danielmorales.validatorx.model;

import com.danielmorales.validatorx.annotations.NotNull;
import com.danielmorales.validatorx.annotations.Email;
import com.danielmorales.validatorx.annotations.Size;

/**
 * Represents a user model with validation constraints.
 * This class is annotated with validation rules to enforce data integrity.
 */
public class User {

    /** User's name, which must not be null. */
    @NotNull(message = "Name cannot be null!")
    private String name;

    /** User's email, which must be in a valid email format. */
    @Email(messageKey = "error.invalidEmail")
    private String email;

    /** User's password, which must be between 8 and 20 characters long. */
    @Size(min = 8, max = 20, message = "Password must be 8-20 chars")
    private String password;

    /**
     * Default constructor for creating an empty user instance.
     */
    public User() {}

    /**
     * Creates a user with the specified name, email, and password.
     *
     * @param name     the user's name
     * @param email    the user's email address
     * @param password the user's password
     */
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the user's name.
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the user's email.
     *
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the user's password.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's name.
     *
     * @param name the new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the user's email.
     *
     * @param email the new email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the user's password.
     *
     * @param password the new password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
