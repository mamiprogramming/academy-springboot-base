package com.spring.springbootapplication.dao;

import com.spring.springbootapplication.entity.LearningData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LearningDataMapper {

    // 指定ユーザー・月のデータを取得
    @Select("SELECT * FROM learning_data WHERE user_id = #{userId} AND learning_month = #{learningMonth}")
    List<LearningData> findByUserIdAndMonth(int userId, String learningMonth);

}