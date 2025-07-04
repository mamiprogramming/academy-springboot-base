package com.spring.springbootapplication.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopController {

    @GetMapping("/top")
    public String showTopPage(Model model, HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");

        if (loginUser == null) {
            // 未ログインの場合はログイン画面へリダイレクト
            return "redirect:/login";
        }

        // ログイン済みなら画面表示
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("isLoginPage", false);
        return "top"; // resources/templates/top.html をレンダリング
    }
}