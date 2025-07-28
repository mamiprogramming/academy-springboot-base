package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TopController {

    // トップページ表示
    @GetMapping("/top")
    public String showTopPage(Model model, HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        // ログイン中ユーザー情報を取り出す
        User user = (User) loginUser;
        model.addAttribute("loginUser", user);
        model.addAttribute("profileName", user.getName());
        model.addAttribute("bio", user.getBio());
        model.addAttribute("isLoginPage", false);

        // 画像バイナリがあればtrueを渡す
        model.addAttribute("hasImageData", user.getImageData() != null);

        return "top";
    }

    // プロフィール画像をバイナリで返すAPI
    @GetMapping("/profile/image")
    @ResponseBody
    public ResponseEntity<byte[]> getProfileImage(HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null || user.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        // 画像の種類に応じてMediaTypeを変える（ここはPNG想定）
        headers.setContentType(MediaType.IMAGE_PNG);
        return ResponseEntity.ok().headers(headers).body(user.getImageData());
    }
}