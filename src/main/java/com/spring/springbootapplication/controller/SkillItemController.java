package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.dto.LearningItemForm;
import com.spring.springbootapplication.service.CategoryService;
import com.spring.springbootapplication.service.SkillItemService;
import com.spring.springbootapplication.service.UserService;
import com.spring.springbootapplication.entity.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/skill")
@RequiredArgsConstructor
public class SkillItemController {

    private final SkillItemService skillItemService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final HttpSession session;

    @GetMapping("/new")
    public String showNewForm(
            @RequestParam String month,
            @RequestParam Integer categoryId, 
            @ModelAttribute("form") LearningItemForm form,
            Model model
    ) {
        // ★ 未ログインならログインへ
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        // 初期値をフォームに詰める
        form.setLearningMonth(month);
        form.setCategoryId(categoryId); 
        model.addAttribute("selectedMonth", month);
        model.addAttribute("isLoginPage", false);
        model.addAttribute("categoryName", categoryService.getNameById(categoryId));
        return "skill_new";
    }

    @PostMapping("/new")
    public String create(
            @Valid @ModelAttribute("form") LearningItemForm form,
            BindingResult bindingResult,
            RedirectAttributes ra,
            Model model
    ) {
        // ★ 未ログインならログインへ
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }
        final int userId = currentUser.getId().intValue();

        // ★ 入力正規化（前後スペース除去）
        if (form.getItem() != null) {
            form.setItem(form.getItem().trim());
        }

        // 同月・同ユーザでの重複チェック
        if (!bindingResult.hasErrors()
                && skillItemService.existsSameItemInMonth(userId, form.getLearningMonth(), form.getItem())) {
            bindingResult.rejectValue("item", "duplicate",
                form.getItem() + "は既に登録されています");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("selectedMonth", form.getLearningMonth());
            model.addAttribute("isLoginPage", false);
            // ★ エラーで戻すときも再セット（これを忘れると null になります）
            model.addAttribute("categoryName", categoryService.getNameById(form.getCategoryId()));
            return "skill_new";
        }

        // 登録（Service 側のシグネチャも int 受け取りに統一）
        skillItemService.create(userId, form);

        // 完了モーダル表示用フラッシュ
        ra.addFlashAttribute("saved", true);
        ra.addFlashAttribute("month", form.getLearningMonth());
        ra.addFlashAttribute("categoryId", form.getCategoryId());

        // 同じ /skill/new にリダイレクトしてモーダルを出す
        return "redirect:/skill/new?month=" + form.getLearningMonth()
                + "&categoryId=" + form.getCategoryId();
    }

    @GetMapping(value = "/new", params = "!month")
    public String handleNewWithoutMonth(
            @RequestParam(required = false) Integer categoryId
    ) {
        User u = userService.getCurrentUser(session);
        if (u == null) return "redirect:/login";

        String month = (String) session.getAttribute("selectedMonth");
        if (month == null || month.isBlank()) {
            month = java.time.YearMonth.now(java.time.ZoneId.of("Asia/Tokyo")).toString();
        }

        if (categoryId == null) {
            var list = categoryService.findAllOrderById();
            if (list.isEmpty()) return "redirect:/";
            categoryId = list.get(0).getId();
        }
        return "redirect:/skill/new?month=" + month + "&categoryId=" + categoryId;
    }
}