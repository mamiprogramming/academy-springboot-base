package com.spring.springbootapplication.dao;

import com.spring.springbootapplication.entity.LearningData;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LearningDataMapper {

    // 既存分：指定ユーザー・月のデータ取得
    @Select("""
        SELECT *
          FROM learning_data
         WHERE user_id = #{userId}
           AND learning_month = #{learningMonth}
      ORDER BY category_id ASC, id ASC   -- ★ 並びを固定
    """)
    List<LearningData> findByUserIdAndMonth(@Param("userId") int userId,
                                            @Param("learningMonth") String learningMonth);

    // 既存分：同一ユーザー×同一月×同一項目の重複チェック
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

    // 既存分：登録
    @Insert("""
        INSERT INTO learning_data
          (user_id, category_id, item, learning_month, learning_time, created_at, updated_at)
        VALUES
          (#{userId}, #{categoryId}, #{item}, #{learningMonth}, #{learningTime}, NOW(), NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(LearningData data);

    // 所有者チェック付きで1件取得（編集対象の存在確認）
    @Select("""
        SELECT *
          FROM learning_data
         WHERE id = #{id}
           AND user_id = #{userId}
    """)
    LearningData findByIdAndUserId(@Param("id") int id, @Param("userId") int userId);

    // 学習時間の更新（所有者縛り）
    @Update("""
        UPDATE learning_data
           SET learning_time = #{learningTime},
               updated_at = NOW()
         WHERE id = #{id}
           AND user_id = #{userId}
    """)
    int updateLearningTime(@Param("id") int id,
                           @Param("userId") int userId,
                           @Param("learningTime") int learningTime);
    
    // 削除
    @Delete("""
        DELETE FROM learning_data
        WHERE id = #{id}
          AND user_id = #{userId}
    """)
    int deleteByIdAndUserId(@Param("id") int id, @Param("userId") int userId);
}