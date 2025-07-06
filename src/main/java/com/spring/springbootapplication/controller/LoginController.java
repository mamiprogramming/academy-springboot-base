package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.dto.LoginForm;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    // ===========================
    // 【開発用】ログイン画面をスキップしてTOPへ
    // ===========================
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // 無条件リダイレクト
        return "redirect:/top";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute("loginForm") LoginForm loginForm,
            HttpSession session,
            Model model) {

        // ===========================
        // 【開発用】ログインチェックもスキップ
        // ===========================
        User dummyUser = new User();
        dummyUser.setId(1); // Integer
        dummyUser.setEmail("dummy@example.com");
        dummyUser.setPassword("dummy");
        session.setAttribute("loginUser", dummyUser);
        return "redirect:/top";

        /*
        // ===========================
        // 【本番用】通常のログイン処理
        // ===========================
        model.addAttribute("isLoginPage", true); // エラー時の画面制御のため

        User user = userService.findByEmail(loginForm.getEmail());

        if (user == null || !user.getPassword().equals(loginForm.getPassword())) {
            model.addAttribute("errorMessage", "メールアドレス、もしくはパスワードが間違っています");
            return "login";
        }

        session.setAttribute("loginUser", user);
        return "redirect:/top";
        */
    }
}