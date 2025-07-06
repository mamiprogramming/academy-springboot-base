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
        // ログインチェックを一時的に無効化
        /*
        Object loginUser = session.getAttribute("loginUser");

        if (loginUser == null) {
            // 未ログインの場合はログイン画面へリダイレクト
            return "redirect:/login";
        }
        */

        // 仮ユーザーを入れる（必要なら）
        User dummyUser = new User();
        dummyUser.setId(1);
        dummyUser.setEmail("dummy@example.com");
        dummyUser.setPassword("dummy");
        session.setAttribute("loginUser", dummyUser);

        model.addAttribute("loginUser", dummyUser);
        model.addAttribute("isLoginPage", false);

        // プロフィール画像URLを渡す
        String profileImageUrl = dummyUser.getProfileImageUrl();
        model.addAttribute("profileImageUrl", profileImageUrl);

        return "top"; // resources/templates/top.html をレンダリング
    }
}