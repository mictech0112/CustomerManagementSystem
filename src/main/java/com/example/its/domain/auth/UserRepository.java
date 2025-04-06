package com.example.its.domain.auth;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);

    List<User> findAll();

    void insert(String username, String password, String authority);

    List<User> searchUsers(String keyword);

    void update(User user);

    void delete(String username);

    // 論理削除を行うメソッドの追加
    void logicalDelete(long userId);
}