package com.spring.springbootapplication.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginForm {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    // Getter / Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}