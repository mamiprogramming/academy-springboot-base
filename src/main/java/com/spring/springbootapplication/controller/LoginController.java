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
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    // ログイン画面の表示
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        model.addAttribute("isLoginPage", true); // ヘッダー制御などに使用
        return "login";
    }

    // ログイン処理
    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("loginForm") LoginForm loginForm,
            BindingResult bindingResult,
            HttpSession session,
            Model model) {

        model.addAttribute("isLoginPage", true);

        // バリデーションエラーがあればログイン画面に戻す
        if (bindingResult.hasErrors()) {
            return "login";
        }

        // 入力が正常ならユーザー検索
        User user = userService.findByEmail(loginForm.getEmail());

        // ユーザーが存在しない、またはパスワード不一致
        if (user == null || !user.getPassword().equals(loginForm.getPassword())) {
            model.addAttribute("errorMessage", "メールアドレスまたはパスワードが正しくありません");
            return "login";
        }

        // ログイン成功時：セッションにユーザーを保存
        session.setAttribute("loginUser", user);
        return "redirect:/top";
    }
}