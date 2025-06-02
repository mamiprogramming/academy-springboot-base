package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.form.UserForm;
import com.spring.springbootapplication.domain.User;
import com.spring.springbootapplication.service.UserService;
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
        return "register"; // templates/register.html を表示
    }

    // 登録フォーム送信時
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("userForm") UserForm userForm,
            BindingResult bindingResult,
            Model model) {

        // バリデーションエラーがあれば戻す
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // UserForm → User に変換
        User user = toEntity(userForm);

        // DB保存処理（UserServiceを呼ぶ）
        userService.registerUser(user);

        // TODO: セッションにログイン状態を保存する

        // 登録後、TOPページ（仮）へ遷移
        return "redirect:/top";
    }

    // 簡易トップページへの遷移
    @GetMapping("/top")
    public String showTopPage() {
        return "top"; // templates/top.html を表示
    }

    // ルートURLへのマッピング（/ → /top へリダイレクト）
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/top";
    }

    // UserForm → User 変換メソッド
    private User toEntity(UserForm form) {
        User user = new User();
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setPassword(form.getPassword()); // ここでパスワードハッシュ化も行うべき（TODO）
        return user;
    }
}