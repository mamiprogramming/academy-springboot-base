package com.spring.springbootapplication.dao;

import com.spring.springbootapplication.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users (name, email, password) VALUES (#{name}, #{email}, #{password})")
    void insertUser(User user);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);
}