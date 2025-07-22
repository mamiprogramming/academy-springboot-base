package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.dto.ProfileEditForm;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.service.UserService;

import jakarta.servlet.http.HttpSession;
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

@Controller
@RequestMapping("/profile")
public class ProfileEditController {

    @Autowired
    private UserService userService;

    // アップロードフォルダ（プロジェクト内の static/upload/）
    private static final String UPLOAD_DIR = "src/main/resources/static/upload/";

    // GET：自己紹介編集ページの表示
    @GetMapping("/edit")
    public String showEditForm(Model model, HttpSession session) {
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            // ログインしていない場合の処理（ログインページにリダイレクトなど）
            return "redirect:/login";
        }

        ProfileEditForm form = new ProfileEditForm();
        form.setBio(currentUser.getBio());
        form.setImageFilename(currentUser.getImage());

        model.addAttribute("profileEditForm", form);
        return "profile_edit";
    }

    // POST：自己紹介と画像の保存処理
    @PostMapping("/edit")
    public String submitEdit(
            @Valid @ModelAttribute("profileEditForm") ProfileEditForm form,
            BindingResult bindingResult,
            Model model,
            HttpSession session) throws IOException {

        if (bindingResult.hasErrors()) {
            return "profile_edit";
        }

        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        // 自己紹介をセット
        currentUser.setBio(form.getBio());

        // 画像ファイル処理
        MultipartFile imageFile = form.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            // ファイル名を取得（ユニーク化推奨。ここでは元の名前＋時間で簡易対応）
            String filename = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

            // フォルダがなければ作成
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 保存
            File dest = new File(uploadDir, filename);
            imageFile.transferTo(dest);

            // DBにファイル名を保存
            currentUser.setImage(filename);
        }

        // DB更新処理
        userService.updateProfile(currentUser);

        return "redirect:/"; // TOPへ
    }
}