package com.example.its.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void create(String username, String password, String authority) {
        var encodedPassword = passwordEncoder.encode(password);
        userRepository.insert(username, encodedPassword, authority);
    }

    public List<User> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User findById(long userId) {
        return userRepository.findByUsername(String.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    public void update(String username, String newPassword, String newAuthority) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (StringUtils.hasText(newPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        user.setAuthority(User.Authority.valueOf(newAuthority));

        userRepository.update(user);
    }

    // 論理削除を実行するdeleteメソッド
    public void delete(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.logicalDelete(user.getId());
    }
}