package com.spring.springbootapplication.entity;

import java.time.LocalDateTime;

public class User {

    private Integer id;          // ユーザーID（自動生成される主キー）
    private String name;         // 名前
    private String email;        // メールアドレス
    private String password;     // パスワード（ハッシュ化推奨）
    private LocalDateTime createdAt;  // 登録日時（DBのtimestamp型に対応）

    // コンストラクタ（引数なし）
    public User() {
    }

    // getter / setter

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
