package com.spring.springbootapplication.dao;

import com.spring.springbootapplication.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users (name, email, password) VALUES (#{name}, #{email}, #{password})")
    void insertUser(User user);

}