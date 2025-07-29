package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.dto.ProfileEditForm;
import com.spring.springbootapplication.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/profile")
public class ProfileEditController {

    private final UserService userService;

    public ProfileEditController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/edit")
    public String showEditForm(Model model, HttpSession session) {
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        ProfileEditForm form = new ProfileEditForm();
        form.setBio(currentUser.getBio());
        form.setImageFilename(currentUser.getImageFilename());

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

        MultipartFile uploadedImage = form.getImage();
        if (uploadedImage != null && !uploadedImage.isEmpty()) {
            session.setAttribute("tempImageData", uploadedImage.getBytes());
            session.setAttribute("tempImageFilename", uploadedImage.getOriginalFilename());
        }

        if (bindingResult.hasErrors()) {
            if (session.getAttribute("tempImageFilename") != null) {
                form.setImageFilename((String) session.getAttribute("tempImageFilename"));
            } else {
                form.setImageFilename(currentUser.getImageFilename());
            }
            return "profile_edit";
        }

        currentUser.setBio(form.getBio());

        byte[] imageData = (byte[]) session.getAttribute("tempImageData");
        String imageFilename = (String) session.getAttribute("tempImageFilename");

        if (imageData != null && imageFilename != null) {
            currentUser.setImageData(imageData);
            currentUser.setImageFilename(imageFilename);
            form.setImageFilename(imageFilename);
        }

        session.removeAttribute("tempImageData");
        session.removeAttribute("tempImageFilename");

        userService.updateProfile(currentUser);

        return "redirect:/";
    }
}