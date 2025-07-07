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

    // ログイン画面を表示
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        model.addAttribute("isLoginPage", true);
        return "login";
    }

    // ログイン処理
    @PostMapping("/login")
    public String login(
            @ModelAttribute("loginForm") LoginForm loginForm,
            HttpSession session,
            Model model) {

        model.addAttribute("isLoginPage", true); // ログインページ用の画面制御

        // メールアドレスでユーザーを検索
        User user = userService.findByEmail(loginForm.getEmail());

        // ユーザーが存在しない、またはパスワード不一致
        if (user == null || !user.getPassword().equals(loginForm.getPassword())) {
            model.addAttribute("errorMessage", "メールアドレス、もしくはパスワードが間違っています");
            return "login";
        }

        // ログイン成功
        session.setAttribute("loginUser", user);
        return "redirect:/top";
    }
}