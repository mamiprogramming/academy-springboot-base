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
        if (!(loginUser instanceof User user)) {
            return "redirect:/login";
        }

        model.addAttribute("loginUser", user);
        model.addAttribute("profileName", user.getName());
        model.addAttribute("bio", user.getBio());
        model.addAttribute("isLoginPage", false);

        // 画像があるかフラグ
        boolean hasImage = user.getImageData() != null && user.getImageData().length > 0;
        model.addAttribute("hasImageData", hasImage);

        return "top";
    }

    // プロフィール画像を返す（画像がない場合も透明の空画像で返す）
    @GetMapping("/profile/image")
    @ResponseBody
    public ResponseEntity<byte[]> getProfileImage(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");

        if (!(loginUser instanceof User user)) {
            return transparentImageResponse();
        }

        byte[] imageData = user.getImageData();
        if (imageData == null || imageData.length == 0) {
            return transparentImageResponse();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return ResponseEntity.ok().headers(headers).body(imageData);
    }

    // 空の透明画像（0バイト PNG）を返す
    private ResponseEntity<byte[]> transparentImageResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return ResponseEntity.ok().headers(headers).body(new byte[0]);
    }
}