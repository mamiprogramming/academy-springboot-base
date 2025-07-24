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

    User currentUser = userService.getCurrentUser(session);
    if (currentUser == null) {
        return "redirect:/login";
    }

    // 画像がアップロードされていない場合、フォームの画像名がnullまたは空なら
    // 現ユーザーの画像名をフォームにセットしておく（バリデーションエラー時の再表示用）
    if ((form.getImage() == null || form.getImage().isEmpty())
         && (form.getImageFilename() == null || form.getImageFilename().isEmpty())) {
        form.setImageFilename(currentUser.getImage());
    }

    if (bindingResult.hasErrors()) {
        // エラー時はフォームの画像ファイル名をセットしたまま再表示するため、
        // currentUserの画像名が空でなければフォームにセット（2回目以降の画面表示で消えないように）
        if (form.getImageFilename() == null || form.getImageFilename().isEmpty()) {
            form.setImageFilename(currentUser.getImage());
        }
        return "profile_edit";
    }

    // 自己紹介更新
    currentUser.setBio(form.getBio());

    MultipartFile imageFile = form.getImage();
    if (imageFile != null && !imageFile.isEmpty()) {
        // 以下画像保存処理はそのまま
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFilename = imageFile.getOriginalFilename();
        String safeFilename = originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
        String filename = System.currentTimeMillis() + "_" + safeFilename;

        BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());

        int targetSize = 360;

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

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

        BufferedImage croppedImage = originalImage.getSubimage(cropStartX, cropStartY, cropSize, cropSize);

        BufferedImage resizedImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, targetSize, targetSize);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(croppedImage, 0, 0, targetSize, targetSize, null);
        g.dispose();

        File dest = new File(dir, filename);

        ImageIO.write(resizedImage, "jpg", dest);

        currentUser.setImage(filename);
    }

    userService.updateProfile(currentUser);

    return "redirect:/";
  }
}