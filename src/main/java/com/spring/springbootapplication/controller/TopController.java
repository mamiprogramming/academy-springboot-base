package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.dao.LearningDataMapper;
import com.spring.springbootapplication.dto.MonthCategoryTotal;
import com.spring.springbootapplication.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class TopController {

    private final LearningDataMapper learningDataMapper;

    public TopController(LearningDataMapper learningDataMapper) {
        this.learningDataMapper = learningDataMapper;
    }

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

        /* ====== ここから追加：トップにもチャート用データを積む ====== */
        // 表示月（yyyy-MM）: 指定が無いので「今月」を基準に直近3ヶ月を出す
        String month = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        YearMonth selectedYM = YearMonth.parse(month);

        // 先々月, 先月, 今月（yyyy-MM）
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

        // DB集計（※LearningDataMapper 側で「削除分除外」「INT厳守」を実装済み想定）
        List<MonthCategoryTotal> rows =
                learningDataMapper.sumByUserAndMonths(user.getId(), monthsForChart);

        // 月×カテゴリ（1:BE, 2:FE, 3:Infra）を0埋めで格納
        Map<String, Map<Integer, Integer>> grid = new HashMap<>();
        for (String m : monthsForChart) {
            grid.put(m, new HashMap<>(Map.of(1, 0, 2, 0, 3, 0)));
        }
        for (MonthCategoryTotal r : rows) {
            grid.get(r.getLearningMonth()).put(r.getCategoryId(), r.getTotal());
        }

        // データセット作成
        List<Integer> be = new ArrayList<>();
        List<Integer> fe = new ArrayList<>();
        List<Integer> infra = new ArrayList<>();
        int maxVal = 0;
        for (String m : monthsForChart) {
            int v1 = grid.get(m).getOrDefault(1, 0);
            int v2 = grid.get(m).getOrDefault(2, 0);
            int v3 = grid.get(m).getOrDefault(3, 0);
            be.add(v1); fe.add(v2); infra.add(v3);
            maxVal = Math.max(maxVal, Math.max(v1, Math.max(v2, v3)));
        }
        int yMax = Math.max(100, (int) Math.ceil(maxVal / 10.0) * 10);

        // ★ テンプレが読む属性名（これが無いと棒が出ません）
        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("datasetBackend", be);
        model.addAttribute("datasetFrontend", fe);
        model.addAttribute("datasetInfra", infra);
        model.addAttribute("chartYMax", yMax);

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