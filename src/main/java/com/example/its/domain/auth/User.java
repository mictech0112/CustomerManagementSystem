package com.example.its.domain.auth;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {

    private String username;
    private String password;
    private Authority authority;
    private long id;
    private LocalDateTime deletedAt; // 論理削除用のフィールドを追加

    public enum Authority {
        ADMIN,
        USER,
        DEFAULT_VALUE
    }

    public User(String username, String password, Authority authority) {
        this.username = username;
        this.password = password;
        this.authority = authority;
    }

    public User() {
    }

    public long getId() {
        return id;
    }
}