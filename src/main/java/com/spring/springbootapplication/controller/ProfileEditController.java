package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.dto.ProfileEditForm;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

@Controller
@RequestMapping("/profile")
public class ProfileEditController {

    @Autowired
    private UserService userService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/edit")
    public String showEditForm(Model model, HttpSession session) {
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        ProfileEditForm form = new ProfileEditForm();
        form.setBio(currentUser.getBio());
        form.setImageFilename(currentUser.getImage());

        model.addAttribute("profileEditForm", form);
        return "profile_edit";
    }

    @PostMapping("/edit")
    public String submitEdit(
            @Valid @ModelAttribute("profileEditForm") ProfileEditForm form,
            BindingResult bindingResult,
            Model model,
            HttpSession session) throws IOException {

        if (bindingResult.hasErrors()) {
            return "profile_edit";
        }

        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        // 自己紹介更新
        currentUser.setBio(form.getBio());

        MultipartFile imageFile = form.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String originalFilename = imageFile.getOriginalFilename();
            String safeFilename = originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
            String filename = System.currentTimeMillis() + "_" + safeFilename;

            // 画像読み込み
            BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());

            // ターゲットサイズ（正方形360x360）
            int targetSize = 360;

            // 元画像の縦横比を計算
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            // トリミングする領域のサイズと開始位置（中心から正方形で切り抜く）
            int cropSize;
            int cropStartX;
            int cropStartY;

            if (originalWidth > originalHeight) {
                cropSize = originalHeight;
                cropStartX = (originalWidth - cropSize) / 2;
                cropStartY = 0;
            } else {
                cropSize = originalWidth;
                cropStartX = 0;
                cropStartY = (originalHeight - cropSize) / 2;
            }

            // 中心の正方形を切り抜く
            BufferedImage croppedImage = originalImage.getSubimage(cropStartX, cropStartY, cropSize, cropSize);

            // リサイズ画像作成
            BufferedImage resizedImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_RGB);

            Graphics2D g = resizedImage.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.setColor(Color.WHITE);  // 背景白で塗りつぶし（透過を白に変換）
            g.fillRect(0, 0, targetSize, targetSize);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(croppedImage, 0, 0, targetSize, targetSize, null);
            g.dispose();

            File dest = new File(dir, filename);

            // JPEGで保存
            ImageIO.write(resizedImage, "jpg", dest);

            // Userエンティティにセット
            currentUser.setImage(filename);
        }

        userService.updateProfile(currentUser);

        return "redirect:/";
    }
}