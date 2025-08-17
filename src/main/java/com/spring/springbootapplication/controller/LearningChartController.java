package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.entity.Category;              // ★ ここを自分のパッケージに合わせる
import com.spring.springbootapplication.service.LearningChartService;
import com.spring.springbootapplication.service.UserService;
import com.spring.springbootapplication.service.CategoryService;      // ★ 追加

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import static java.util.stream.Collectors.groupingBy;

@Controller
public class LearningChartController {

    private final UserService userService;
    private final LearningChartService learningChartService;
    private final CategoryService categoryService;                   // ★ 追加

    // ★ コンストラクタは1つに統一（Springが自動DI）
    public LearningChartController(UserService userService,
                                   LearningChartService learningChartService,
                                   CategoryService categoryService) {
        this.userService = userService;
        this.learningChartService = learningChartService;
        this.categoryService = categoryService;
    }

    /**
     * 学習チャート表示ページ
     * 初期表示 or 月変更後に使用
     */
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

        // category_id ごとにグルーピング（Map<categoryId, List<LearningData>>）
        Map<Integer, List<LearningData>> byCat =
            dataList.stream()
                    .filter(Objects::nonNull)
                    .collect(groupingBy(LearningData::getCategoryId));

        // 月プルダウンデータ
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
}
