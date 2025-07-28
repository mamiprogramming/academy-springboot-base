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

import java.io.IOException;

@Controller
public class ProfileEditController {

    @Autowired
    private UserService userService;

    // 編集画面表示
    @GetMapping("/profile/edit")
    public String showEditForm(Model model, HttpSession session) {
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        ProfileEditForm form = new ProfileEditForm();
        form.setBio(currentUser.getBio());
        form.setImageFilename(currentUser.getImageFilename());

        model.addAttribute("profileEditForm", form);
        return "profile_edit";
    }

    // 編集内容送信処理
    @PostMapping("/profile/edit")
    public String submitEdit(
            @Valid @ModelAttribute("profileEditForm") ProfileEditForm form,
            BindingResult bindingResult,
            Model model,
            HttpSession session) throws IOException {

        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        // バリデーションエラー時、ファイル名が消えないように保持
        if (bindingResult.hasErrors()) {
            if ((form.getImage() == null || form.getImage().isEmpty())
                    && (form.getImageFilename() == null || form.getImageFilename().isEmpty())) {
                form.setImageFilename(currentUser.getImageFilename());
            }
            return "profile_edit";
        }

        // 自己紹介更新
        currentUser.setBio(form.getBio());

        // 画像アップロード処理
        if (form.getImage() != null && !form.getImage().isEmpty()) {
            currentUser.setImageData(form.getImage().getBytes());
            currentUser.setImageFilename(form.getImage().getOriginalFilename());
            form.setImageFilename(form.getImage().getOriginalFilename());
        } else {
            // 画像アップロードなしなら既存画像情報を維持
            form.setImageFilename(currentUser.getImageFilename());
        }

        userService.updateProfile(currentUser);

        return "redirect:/";
    }
}