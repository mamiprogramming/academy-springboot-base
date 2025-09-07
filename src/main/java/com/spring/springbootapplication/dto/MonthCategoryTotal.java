package com.spring.springbootapplication.dto;

public class MonthCategoryTotal {
    private String learningMonth; // "YYYY-MM"
    private Integer categoryId;
    private Integer total;

    public String getLearningMonth() { return learningMonth; }
    public void setLearningMonth(String learningMonth) { this.learningMonth = learningMonth; }
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
}