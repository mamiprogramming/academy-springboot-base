package com.spring.springbootapplication.service;

import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.dao.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;

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

    // メールアドレスで検索
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    // ログイン中のユーザーをHttpSessionから取得
    public User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("loginUser");
    }

    // 自己紹介・画像の更新
    @Transactional
    public void updateProfile(User user) {
        userMapper.updateUserProfile(user);
    }
}