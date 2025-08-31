package com.spring.springbootapplication.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LearningTimeEditForm {
    @NotNull private Integer id;
    @NotNull @Min(value = 0, message = "学習時間は0以上の数字で入力してください")
    private Integer learningTime;
    @NotBlank private String month;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getLearningTime() { return learningTime; }
    public void setLearningTime(Integer learningTime) { this.learningTime = learningTime; }
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
}