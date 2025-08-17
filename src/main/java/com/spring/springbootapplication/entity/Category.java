package com.spring.springbootapplication.entity;

import java.time.LocalDateTime;

public class Category {
    private Integer id;                 // categories.id
    private String categoryName;        // categories.category_name
    private LocalDateTime createdAt;    // categories.created_at
    private LocalDateTime updatedAt;    // categories.updated_at

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}