package com.spring.springbootapplication.service;

import com.spring.springbootapplication.entity.Category;
import com.spring.springbootapplication.dao.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public List<Category> findAllOrderById() {
        return categoryMapper.findAllOrderById();
    }

      // ★ idからカテゴリ名を取得（int/Integerどちらでも呼べる）
    public String getNameById(int id) {
        Category c = categoryMapper.findById(id);
        return (c != null && c.getCategoryName() != null) ? c.getCategoryName() : "";
    }
    public String getNameById(Integer id) {
        return (id == null) ? "" : getNameById(id.intValue());
    }
}