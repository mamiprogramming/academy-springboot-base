package com.spring.springbootapplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

public class ProfileEditForm {

    @Size(min = 50, max = 200, message = "自己紹介は50文字以上200文字以下で入力してください")
    private String bio;

    // 画像ファイル（アップロード用）
    private MultipartFile image;

    // 編集時に既存の画像ファイル名を保持するためのフィールド
    private String imageFilename;

    // getter/setter
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }

    public MultipartFile getImage() {
        return image;
    }
    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getImageFilename() {
        return imageFilename;
    }
    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }
}