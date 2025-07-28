package com.spring.springbootapplication.dao;

import com.spring.springbootapplication.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users (name, email, password) VALUES (#{name}, #{email}, #{password})")
    void insertUser(User user);

    @Select("SELECT id, name, email, password, bio, image, image_data AS imageData FROM users WHERE email = #{email}")
    User findByEmail(String email);

    // 自己紹介と画像の更新
    @Update("UPDATE users SET bio = #{bio}, image = #{image}, image_data = #{imageData} WHERE email = #{email}")
    void updateUserProfile(User user);
}