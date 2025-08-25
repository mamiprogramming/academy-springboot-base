package com.spring.springbootapplication.dao;

import com.spring.springbootapplication.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("""
        SELECT
          id,
          category_name AS categoryName,
          created_at    AS createdAt,
          updated_at    AS updatedAt
        FROM categories
        ORDER BY id
    """)
    List<Category> findAllOrderById();

    // ★ 追加：主キーで1件取得
    @Select("""
        SELECT
          id,
          category_name AS categoryName,
          created_at    AS createdAt,
          updated_at    AS updatedAt
        FROM categories
        WHERE id = #{id}
    """)
    Category findById(@Param("id") int id);
}