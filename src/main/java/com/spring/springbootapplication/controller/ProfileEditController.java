package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.dto.ProfileEditForm;
import com.spring.springbootapplication.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/profile")
public class ProfileEditController {

    private final UserService userService;

    public ProfileEditController(UserService userService) {
        this.userService = userService;
    }

    // 編集画面表示(GET)
    @GetMapping("/edit")
    public String showEditForm(Model model, HttpSession session) {
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        ProfileEditForm form = new ProfileEditForm();
        form.setBio(currentUser.getBio());

        // セッションに一時保存された画像があれば優先表示
        if (session.getAttribute("tempImageFilename") != null) {
            form.setImageFilename((String) session.getAttribute("tempImageFilename"));
        } else {
            // なければDBの画像ファイル名を表示
            form.setImageFilename(currentUser.getImageFilename());
        }

        model.addAttribute("profileEditForm", form);
        return "profile_edit";
    }

    // 編集内容送信(POST)
    @PostMapping("/edit")
    public String submitEdit(
            @Valid @ModelAttribute("profileEditForm") ProfileEditForm form,
            BindingResult bindingResult,
            Model model,
            HttpSession session) throws IOException {

        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        MultipartFile uploadedImage = form.getImage();

        // アップロードされた画像があればセッションに保存（バリデーションエラー時に使う）
        if (uploadedImage != null && !uploadedImage.isEmpty()) {
            session.setAttribute("tempImageData", uploadedImage.getBytes());
            session.setAttribute("tempImageFilename", uploadedImage.getOriginalFilename());
        }

        if (bindingResult.hasErrors()) {
            // バリデーションエラー時は画像ファイル名を消さない
            if (session.getAttribute("tempImageFilename") != null) {
                form.setImageFilename((String) session.getAttribute("tempImageFilename"));
            } else {
                form.setImageFilename(currentUser.getImageFilename());
            }
            return "profile_edit";
        }

        // 自己紹介を更新
        currentUser.setBio(form.getBio());

        // 画像データはセッションから取得（アップロードあれば）
        byte[] imageData = (byte[]) session.getAttribute("tempImageData");
        String imageFilename = (String) session.getAttribute("tempImageFilename");

        if (imageData != null && imageFilename != null) {
            currentUser.setImageData(imageData);
            currentUser.setImageFilename(imageFilename);
            form.setImageFilename(imageFilename);
        } else {
            // 画像アップロードがないなら既存のまま維持
            form.setImageFilename(currentUser.getImageFilename());
        }

        // セッションの画像情報はクリア
        session.removeAttribute("tempImageData");
        session.removeAttribute("tempImageFilename");

        // DB更新
        userService.updateProfile(currentUser);

        return "redirect:/";
    }
}
