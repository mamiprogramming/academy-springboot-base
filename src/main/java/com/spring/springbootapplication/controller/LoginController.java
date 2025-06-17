package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = userService.findByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            // Flashでエラーメッセージを登録してリダイレクト
            redirectAttributes.addFlashAttribute("errorMessage", "メールアドレス、もしくはパスワードが間違っています");
            return "redirect:/login";
        }

        // 認証成功 → セッションにユーザー保存
        session.setAttribute("loginUser", user);

        // TOPへリダイレクト（簡易TOP画面想定）
        return "redirect:/top";
    }

    @GetMapping("/top")
    public String showTopPage() {
        return "top"; // 簡易TOPページ（テンプレートtop.html）
    }
}