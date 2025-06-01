package com.spring.app.controller;

import com.spring.app.form.UserForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

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

        // TODO: ユーザーのDB保存処理をここで行う

        // TODO: セッションにログイン状態を保存する

        // 登録後、TOPページ（仮）へ遷移
        return "redirect:/top";
    }

    //簡易トップページへの遷移
    @GetMapping("/top")
    public String showTopPage() {
        return "top"; // templates/top.html を表示
    }
}
