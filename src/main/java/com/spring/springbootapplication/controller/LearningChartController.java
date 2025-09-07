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

import com.spring.springbootapplication.dto.MonthCategoryTotal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        model.addAttribute("month", month); 
        model.addAttribute("selectedMonthLabel", selectedMonthLabel);
        model.addAttribute("selectedMonthNumber", selectedMonthNumber);
        model.addAttribute("availableMonths", availableMonths);
        model.addAttribute("availableMonthsLabel", availableMonthsLabel);
        model.addAttribute("isLoginPage", false);

        /* --- チャート用データ --- */

        // 選択月から直近3か月（先々月, 先月, 今月）
        YearMonth selectedYM = YearMonth.parse(month);
        List<String> monthsForChart = List.of(
                selectedYM.minusMonths(2).toString(),
                selectedYM.minusMonths(1).toString(),
                selectedYM.toString()
        );

        // 表示ラベル（例: "6月","7月","8月"）
        DateTimeFormatter jp = DateTimeFormatter.ofPattern("M月");
        List<String> chartLabels = monthsForChart.stream()
                .map(m -> YearMonth.parse(m).format(jp))
                .toList();

        // 月×カテゴリ合計（INT厳守：Mapperは LEAST(SUM(...),2147483647)::int）
        List<MonthCategoryTotal> rows =
                learningDataMapper.sumByUserAndMonths(loginUser.getId(), monthsForChart);

        // 0埋めグリッド（1:BE, 2:FE, 3:Infra）
        Map<String, Map<Integer, Integer>> grid = new HashMap<>();
        for (String m : monthsForChart) {
            grid.put(m, new HashMap<>(Map.of(1, 0, 2, 0, 3, 0)));
        }
        for (MonthCategoryTotal r : rows) {
            grid.get(r.getLearningMonth()).put(r.getCategoryId(), r.getTotal());
        }

        // データセットとY最大値（最低100、10刻み切り上げ）
        List<Integer> be = new ArrayList<>(), fe = new ArrayList<>(), infra = new ArrayList<>();
        int maxVal = 0;
        for (String m : monthsForChart) {
            int v1 = grid.get(m).getOrDefault(1, 0);
            int v2 = grid.get(m).getOrDefault(2, 0);
            int v3 = grid.get(m).getOrDefault(3, 0);
            be.add(v1); fe.add(v2); infra.add(v3);
            maxVal = Math.max(maxVal, Math.max(v1, Math.max(v2, v3)));
        }
        int yMax = Math.max(100, maxVal);
        yMax = ((int) Math.ceil(yMax / 10.0)) * 10;

        // Viewへ（Chart.js が読む属性名）
        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("datasetBackend", be);
        model.addAttribute("datasetFrontend", fe);
        model.addAttribute("datasetInfra", infra);
        model.addAttribute("chartYMax", yMax);

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
        ra.addFlashAttribute("updateSaved", true);
        ra.addFlashAttribute("savedItemName", data.getItem());
        ra.addFlashAttribute("savedTime", form.getLearningTime());
        ra.addFlashAttribute("categoryName", categoryService.getNameById(data.getCategoryId()));
        ra.addFlashAttribute("month", form.getMonth());

        // 一覧へ戻る（GETで saved を拾ってモーダル表示）
        ra.addAttribute("month", form.getMonth());
        return "redirect:/skill/edit";
    }

    /** 削除 */
    @PostMapping("/skill/delete")
    public String deleteItem(@RequestParam("id") int id,
                             @RequestParam("month") String month,
                             HttpSession session,
                             RedirectAttributes ra) {
        User user = userService.getCurrentUser(session);
        if (user == null) return "redirect:/login";

        // 所有者チェック → 削除
        LearningData data = learningDataMapper.findByIdAndUserId(id, user.getId());
        if (data != null) {
            learningDataMapper.deleteByIdAndUserId(id, user.getId());
            // HTMLのフラッシュ参照に合わせる
            ra.addFlashAttribute("deleteDone", true);
            ra.addFlashAttribute("deletedItemName", data.getItem());
        }

        // 同じ月に戻る
        ra.addAttribute("month", month);
        return "redirect:/skill/edit";
    }
}
