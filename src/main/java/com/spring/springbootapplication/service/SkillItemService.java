package com.spring.springbootapplication.service;

import com.spring.springbootapplication.dao.LearningDataMapper;
import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.dto.LearningItemForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SkillItemService {

    private final LearningDataMapper learningDataMapper;

    public boolean existsSameItemInMonth(int userId, String month, String item) {
        return learningDataMapper.countByUserAndMonthAndItem(userId, month, item) > 0;
    }

    @Transactional
    public void create(int userId, LearningItemForm form) {
        LearningData ld = new LearningData();
        ld.setUserId(userId);
        ld.setCategoryId(form.getCategoryId());
        ld.setItem(form.getItem());
        ld.setLearningMonth(form.getLearningMonth());
        ld.setLearningTime(form.getLearningTime());
        learningDataMapper.insert(ld);
    }
}