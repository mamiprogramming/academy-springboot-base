package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopController {

    @GetMapping("/top")
    public String showTopPage(Model model, HttpSession session) {
        // ログインしているか確認
        Object loginUser = session.getAttribute("loginUser");

        if (loginUser == null) {
            // 未ログインの場合はログイン画面へリダイレクト
            return "redirect:/login";
        }

        // ログイン中ユーザー情報を取り出す
        User user = (User) loginUser;

        model.addAttribute("loginUser", user);
        model.addAttribute("isLoginPage", false);

        // プロフィール画像URLを渡す
        String profileImageUrl = user.getProfileImageUrl();
        model.addAttribute("profileImageUrl", profileImageUrl);

        return "top"; // resources/templates/top.html をレンダリング
    }
}