package com.spring.springbootapplication.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

@Controller
@RequestMapping("/profile")
public class ProfileEditController {

    @Autowired
    private UserService userService;  // ユーザーデータ操作サービス（例）

    // 画像保存先ディレクトリ（環境に応じて変更）
    private static final String UPLOAD_DIR = "upload/";

    // GET：編集画面表示
    @GetMapping("/edit")
    public String showEditForm(Model model) {
        // ログインユーザーの情報取得例
        User currentUser = userService.getCurrentUser();

        // フォームDTOにセット
        ProfileEditForm form = new ProfileEditForm();
        form.setBio(currentUser.getBio());
        form.setImageFilename(currentUser.getImage());

        model.addAttribute("profileEditForm", form);
        return "profile_edit";
    }

    // POST：編集内容送信処理
    @PostMapping("/edit")
    public String submitEdit(
            @Valid @ModelAttribute("profileEditForm") ProfileEditForm form,
            BindingResult bindingResult,
            Model model) throws IOException {

        // バリデーションエラー時は再表示
        if (bindingResult.hasErrors()) {
            return "profile_edit";
        }

        User currentUser = userService.getCurrentUser();

        // 画像ファイルがアップロードされた場合の処理
        MultipartFile imageFile = form.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            // ファイル名を安全に取得（実際はUUID等でユニーク化推奨）
            String filename = imageFile.getOriginalFilename();

            // 保存先のパスを作成（uploadフォルダはあらかじめ作成しておく）
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File dest = new File(uploadDir, filename);
            imageFile.transferTo(dest);  // ファイル保存

            // DBにファイル名をセット
            currentUser.setImage(filename);
        }

        // 自己紹介をセット
        currentUser.setBio(form.getBio());

        // DB更新（サービス経由で）
        userService.updateUser(currentUser);

        // TOPページへリダイレクト
        return "redirect:/";
    }
}