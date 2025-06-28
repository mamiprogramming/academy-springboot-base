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
        model.addAttribute("loginUser", loginUser);
        return "top"; // resources/templates/top.html をレンダリング
    }
}