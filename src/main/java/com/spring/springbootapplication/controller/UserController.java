package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.dto.UserForm;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // 登録フォームを表示
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("isLoginPage", false); // ヘッダー用
        return "register";
    }

    // 登録フォーム送信時
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("userForm") UserForm userForm,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("isLoginPage", false); // ヘッダー用
            return "register";
        }

        // UserForm → User に変換
        User user = toEntity(userForm);

        // DBに保存
        userService.registerUser(user);

        // セッションにログイン状態を保存
        session.setAttribute("loginUser", user);

        // 登録後TOPページへ
        return "redirect:/top";
    }

    // ルートURLへのマッピング（/ → /topへ）
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/top";
    }

    // UserForm→User変換
    private User toEntity(UserForm form) {
        User user = new User();
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setPassword(form.getPassword()); // TODO: ハッシュ化
        return user;
    }
}