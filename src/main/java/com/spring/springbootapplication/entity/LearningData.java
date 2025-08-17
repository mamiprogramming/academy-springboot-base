package com.spring.springbootapplication.entity;

import java.time.LocalDateTime;

public class LearningData {

    private int id;
    private int userId;
    private int categoryId;
    private String item;
    private String learningMonth; // 形式: "2025-05"
    private int learningTime;     // 分単位
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- Getter / Setter ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getLearningMonth() {
        return learningMonth;
    }

    public void setLearningMonth(String learningMonth) {
        this.learningMonth = learningMonth;
    }

    public int getLearningTime() {
        return learningTime;
    }

    public void setLearningTime(int learningTime) {
        this.learningTime = learningTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}