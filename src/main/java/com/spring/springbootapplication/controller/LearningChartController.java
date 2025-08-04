package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.service.LearningChartService;
import com.spring.springbootapplication.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LearningChartController {

    private final UserService userService;
    private final LearningChartService learningChartService;

    public LearningChartController(UserService userService, LearningChartService learningChartService) {
        this.userService = userService;
        this.learningChartService = learningChartService;
    }

    /**
     * 学習チャート表示ページ
     * 初期表示 or 月変更後に使用
     */
    @GetMapping("/skill/edit")
    public String showChartPage(@RequestParam(value = "month", required = false) String month,
                                 HttpSession session,
                                 Model model) {

        // ログイン中のユーザーを取得
        User loginUser = userService.getCurrentUser(session);
        if (loginUser == null) {
            return "redirect:/login"; // 未ログインならログイン画面へ
        }

        // 月が指定されていない場合は当月（yyyy-MM形式）
        if (month == null || month.isEmpty()) {
            month = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
        }

        // 指定月の学習データを取得
        List<LearningData> dataList = learningChartService.getDataByMonth(loginUser.getId(), month);

        // プルダウン用の年月リストを取得
        List<String> availableMonths = learningChartService.getAvailableMonths();
        
        // 月表示ラベルリストを作る
        List<String> availableMonthsLabel = availableMonths.stream()
            .map(m -> {
                int monthNum = Integer.parseInt(m.substring(5,7));
                return monthNum + "月";
            })
            .toList();

        // Viewに渡すデータをModelに追加
        model.addAttribute("learningDataList", dataList);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("availableMonths", availableMonths);
        model.addAttribute("availableMonthsLabel", availableMonthsLabel);

        return "skill_edit"; // 表示先のHTMLテンプレート
    }
}
