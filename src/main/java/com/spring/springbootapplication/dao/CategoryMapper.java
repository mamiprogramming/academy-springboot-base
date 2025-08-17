package com.spring.springbootapplication.dao;

import com.spring.springbootapplication.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
@Mapper
public interface CategoryMapper {
    @Select("""
        SELECT id, category_name, created_at, updated_at
        FROM categories
        ORDER BY id
    """)
    List<Category> findAllOrderById();
}