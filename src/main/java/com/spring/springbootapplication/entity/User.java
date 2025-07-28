package com.spring.springbootapplication.entity;

import java.time.LocalDateTime;

public class User {

    private Integer id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private String bio;
    private String image;          // 画像ファイル名（DBに保存する文字列）
    private String imageFilename;  // 追加

    // 画像バイナリデータをバイト配列で扱う
    private byte[] imageData; // ※ DBにバイナリ保存する

    public User() {}

    // getter / setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getImageFilename() { return imageFilename; }
    public void setImageFilename(String imageFilename) { this.imageFilename = imageFilename; }

    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }
}