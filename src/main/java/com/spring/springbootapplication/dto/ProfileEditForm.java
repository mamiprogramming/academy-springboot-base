package com.spring.springbootapplication.dto;

import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class ProfileEditForm {

    @Size(min = 50, max = 200, message = "自己紹介は50文字以上200文字以下で入力してください")
    private String bio;

    // アップロード画像ファイル
    private MultipartFile image;

    // 画像ファイル名（画面表示用、DB登録用）
    private String imageFilename = ""; // null回避のため空文字で初期化しても良い

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