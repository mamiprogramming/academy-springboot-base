package com.spring.springbootapplication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginForm {

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "メールアドレスの形式が正しくありません")
    private String email;

    @NotBlank(message = "パスワードを入力してください")
    private String password;

    // --- Getter ---
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // --- Setter ---
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}