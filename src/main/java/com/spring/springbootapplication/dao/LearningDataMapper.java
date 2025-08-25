package com.spring.springbootapplication.dao;

import com.spring.springbootapplication.entity.LearningData;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LearningDataMapper {

    // 既存分：指定ユーザー・月のデータ取得
    @Select("SELECT * FROM learning_data WHERE user_id = #{userId} AND learning_month = #{learningMonth}")
    List<LearningData> findByUserIdAndMonth(@Param("userId") int userId,
                                            @Param("learningMonth") String learningMonth);

    // 追加分：同一ユーザー・同一月・同一項目名の重複チェック
    @Select("""
        SELECT COUNT(*)
          FROM learning_data
         WHERE user_id = #{userId}
           AND learning_month = #{month}
           AND item = #{item}
        """)
    int countByUserAndMonthAndItem(@Param("userId") int userId,
                                   @Param("month") String month,
                                   @Param("item") String item);

    // 追加分：登録
    @Insert("""
        INSERT INTO learning_data
          (user_id, category_id, item, learning_month, learning_time, created_at, updated_at)
        VALUES
          (#{userId}, #{categoryId}, #{item}, #{learningMonth}, #{learningTime}, now(), now())
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(LearningData data);
}