package com.danielmorales.validatorx.model;

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

    public User() {}

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
