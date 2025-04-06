package com.example.its.web.user;

import com.example.its.web.validation.UniqueUsername;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UserForm {
    @NotBlank
    @UniqueUsername
    @Size(max = 255)
    private String username;

    @NotBlank
    @Size(min = 8, max = 32)
    private String password;

    @NotBlank
    private String authority;

    // デフォルト値の設定
    public static final String DEFAULT_VALUE = "default";

    public UserForm(String username, String password, String authority) {
        this.username = username;
        this.password = password;
        this.authority = authority;
    }
}