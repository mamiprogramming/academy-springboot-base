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

    /**
     * セッションからログイン中ユーザーを取得
     * @param session HttpSession
     * @return ログインユーザーオブジェクト（無ければnull）
     */
    public User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("loginUser");
    }

    /**
     * 新規ユーザー登録
     * トランザクション管理付き
     */
    @Transactional
    public void registerUser(User user) {
        userMapper.insertUser(user);
    }

    /**
     * メールアドレスでユーザー検索
     * @param email 検索したいメールアドレス
     * @return 該当ユーザー（なければnull）
     */
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    /**
     * 自己紹介や画像などのプロフィール更新
     * トランザクション管理付き
     * @param user 更新するUserオブジェクト
     */
    @Transactional
    public void updateProfile(User user) {
        userMapper.updateUserProfile(user);
    }
}
