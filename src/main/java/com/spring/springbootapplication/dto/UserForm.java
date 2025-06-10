package com.spring.springbootapplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserForm {

    @NotBlank(message = "氏名は必ず入力してください")
    @Size(max = 255, message = "氏名は255文字以内で入力してください")
    private String name;

    @NotBlank(message = "メールアドレスは必ず入力してください")
    @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
    @Pattern(
        regexp = "^[\\x00-\\x7F]+$",
        message = "メールアドレスが正しい形式ではありません"
    )
    private String email;

    @NotBlank(message = "パスワードは必ず入力してください")
    @Size(max = 255, message = "パスワードは255文字以内で入力してください")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
        message = "英数字8文字以上で入力してください"
    )
    private String password;

    // --- Getter/Setter ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
