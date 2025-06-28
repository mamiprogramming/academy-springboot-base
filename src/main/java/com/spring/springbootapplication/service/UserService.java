package com.spring.springbootapplication.service;

import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.dao.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // ユーザー登録
    @Transactional
    public void registerUser(User user) {
        userMapper.insertUser(user);
    }

    // ★ 追加：メールアドレスで検索
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }
}