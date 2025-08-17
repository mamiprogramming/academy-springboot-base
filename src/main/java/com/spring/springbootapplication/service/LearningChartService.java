package com.spring.springbootapplication.service;

import com.spring.springbootapplication.dao.LearningDataMapper;
import com.spring.springbootapplication.entity.LearningData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class LearningChartService {

    private final LearningDataMapper learningDataMapper;

    public LearningChartService(LearningDataMapper learningDataMapper) {
        this.learningDataMapper = learningDataMapper;
    }

    /**
     * 指定ユーザー・年月（yyyy-MM形式）の学習データ一覧を取得する
     * @param userId ユーザーID
     * @param yearMonth 例："2025-08"
     * @return 学習データのリスト
     */
    public List<LearningData> getDataByMonth(int userId, String yearMonth) {
        return learningDataMapper.findByUserIdAndMonth(userId, yearMonth);
    }

    /**
     * 当月を含む過去3ヶ月分の年月リストを作成する
     * 例：["2025-08", "2025-07", "2025-06", "2025-05"]
     * @return 年月リスト
     */
    public List<String> getAvailableMonths() {
        List<String> months = new ArrayList<>();
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (int i = 0; i < 3; i++) {
            LocalDate targetDate = now.minusMonths(i);
            months.add(targetDate.format(formatter));
        }

        return months;
    }
}
