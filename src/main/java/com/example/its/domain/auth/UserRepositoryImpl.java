package com.example.its.domain.auth;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    public UserRepositoryImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public void insert(String username, String password, String authority) {
        User.Authority userAuthority = User.Authority.valueOf(authority.toUpperCase());
        User user = new User(username, password, userAuthority);
        userMapper.insert(user);
    }


    @Override
    public List<User> searchUsers(String keyword) {
        return userMapper.searchUsers(keyword);
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public void delete(String username) {
        userMapper.delete(username);
    }

    // logicalDeleteメソッドの実装
    @Override
    public void logicalDelete(long userId) {
        userMapper.logicalDelete(userId);
    }
}