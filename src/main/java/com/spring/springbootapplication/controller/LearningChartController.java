package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.dao.LearningDataMapper;
import com.spring.springbootapplication.entity.Category;
import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.service.CategoryService;
import com.spring.springbootapplication.service.LearningChartService;
import com.spring.springbootapplication.service.UserService;
import com.spring.springbootapplication.dto.LearningTimeEditForm;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;

@Controller
public class LearningChartController {

    private final UserService userService;
    private final LearningChartService learningChartService;
    private final CategoryService categoryService;
    private final LearningDataMapper learningDataMapper;  // ← 追加

    // ★ コンストラクタは1本に統一
    public LearningChartController(UserService userService,
                                   LearningChartService learningChartService,
                                   CategoryService categoryService,
                                   LearningDataMapper learningDataMapper) {
        this.userService = userService;
        this.learningChartService = learningChartService;
        this.categoryService = categoryService;
        this.learningDataMapper = learningDataMapper;
    }

    /** 学習チャート表示（編集画面） */
    @GetMapping("/skill/edit")
    public String showChartPage(@RequestParam(value = "month", required = false) String month,
                                HttpSession session,
                                Model model) {
        // ログインチェック
        User loginUser = userService.getCurrentUser(session);
        if (loginUser == null) return "redirect:/login";

        // 月（yyyy-MM）
        if (month == null || month.isEmpty()) {
            month = java.time.LocalDate.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
        }
        int monthNum = Integer.parseInt(month.substring(5, 7));
        String selectedMonthLabel = monthNum + "月";
        String selectedMonthNumber = String.valueOf(monthNum);

        // 指定月の学習データ（全カテゴリ）
        List<LearningData> dataList =
                learningChartService.getDataByMonth(loginUser.getId(), month);

        // カテゴリ一覧（id昇順）
        List<Category> categories = categoryService.findAllOrderById();

        // category_id ごとにグルーピング
        Map<Integer, List<LearningData>> byCat =
            dataList.stream()
                    .filter(Objects::nonNull)
                    .collect(groupingBy(LearningData::getCategoryId));

        // 月プルダウン
        List<String> availableMonths = learningChartService.getAvailableMonths();
        List<String> availableMonthsLabel = availableMonths.stream()
                .map(m -> Integer.parseInt(m.substring(5, 7)) + "月")
                .toList();

        // Viewへ
        model.addAttribute("categories", categories);
        model.addAttribute("byCat", byCat);
        model.addAttribute("learningDataList", dataList);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedMonthLabel", selectedMonthLabel);
        model.addAttribute("selectedMonthNumber", selectedMonthNumber);
        model.addAttribute("availableMonths", availableMonths);
        model.addAttribute("availableMonthsLabel", availableMonthsLabel);
        model.addAttribute("isLoginPage", false);

        return "skill_edit";
    }

    /** 学習時間の保存（hiddenフォームからPOST） */
    @PostMapping("/skill/time")
    public String updateTime(@Valid LearningTimeEditForm form,
                             BindingResult br,
                             HttpSession session,
                             RedirectAttributes ra) {

        User current = userService.getCurrentUser(session);
        if (current == null) return "redirect:/login";

        // 入力エラー時は一覧へ戻る
        if (br.hasErrors()) {
            ra.addAttribute("month", form.getMonth());
            return "redirect:/skill/edit";
        }

        // 所有者チェック
        LearningData data = learningDataMapper.findByIdAndUserId(form.getId(), current.getId());
        if (data == null) {
            ra.addAttribute("month", form.getMonth());
            return "redirect:/skill/edit";
        }

        // 更新
        learningDataMapper.updateLearningTime(form.getId(), current.getId(), form.getLearningTime());

        // モーダル用フラッシュ属性
        ra.addFlashAttribute("saved", true);
        ra.addFlashAttribute("savedItem", data.getItem());
        ra.addFlashAttribute("savedTime", form.getLearningTime());
        ra.addFlashAttribute("categoryName", categoryService.getNameById(data.getCategoryId()));
        ra.addFlashAttribute("month", form.getMonth());

        // 一覧へ戻る（GETで saved を拾ってモーダル表示）
        ra.addAttribute("month", form.getMonth());
        return "redirect:/skill/edit";
    }
}
