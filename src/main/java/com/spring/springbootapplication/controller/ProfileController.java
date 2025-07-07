package com.spring.springbootapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    // 簡易版自己紹介編集ページ表示
    @GetMapping("/profile/edit")
    public String showSimpleEdit() {
        return "profile_edit"; // テンプレート名（HTMLファイル名から拡張子を除いたもの）
    }
}