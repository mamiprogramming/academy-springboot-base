package com.spring.springbootapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    // 簡易版自己紹介編集ページ表示
    @GetMapping("/profile/edit")
    public String showSimpleEdit() {
        return "profile_edit"; // templates/profile_edit.html
    }

    // 簡易版スキル編集ページ表示
    @GetMapping("/skill/edit")
    public String showSkillEdit() {
        return "skill_edit"; // templates/skill_edit.html
    }
}