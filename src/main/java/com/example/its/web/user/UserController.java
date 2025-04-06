package com.example.its.web.user;

import com.example.its.domain.auth.User;
import com.example.its.domain.auth.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 手動で追加したコンストラクタ
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showList(Model model) {
        List<User> userList = userService.findAll().stream()
                .filter(user -> user.getDeletedAt() == null)
                .collect(Collectors.toList());
        model.addAttribute("userList", userList);
        return "users/list";
    }

    // CREATE
    @GetMapping("/creationForm")
    public String showCreationForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/creationForm";
    }

    @PostMapping
    public String create(@Validated @ModelAttribute UserForm userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "users/creationForm";
        }

        userService.create(userForm.getUsername(), userForm.getPassword(), userForm.getAuthority());

        return "redirect:/users";
    }

    // UPDATE
    @GetMapping("/{username}/editForm")
    public String showEditForm(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);

        String authority;
        try {
            authority = user.getAuthority().name();
        } catch (NullPointerException e) {
            authority = UserForm.DEFAULT_VALUE; // デフォルトの値を指定
        }

        UserForm userForm = new UserForm(user.getUsername(), "", authority);
        model.addAttribute("userForm", userForm);
        return "users/editForm";
    }

    @PostMapping("/{username}")
    public String update(@PathVariable String username, @Validated @ModelAttribute UserForm userForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/editForm";
        }

        // ユーザーが存在するか確認
        User existingUser = userService.findByUsername(username);
        if (existingUser == null) {
            // ユーザーが存在しない場合の処理を追加（エラーメッセージなど）
            return "redirect:/users"; // または適切なエラーページにリダイレクト
        }

        // ユーザーが存在する場合、更新を試みる
        try {
            userService.update(username, userForm.getPassword(), userForm.getAuthority());
            return "redirect:/users";
        } catch (Exception e) {
            // 更新中にエラーが発生した場合の処理を追加（エラーメッセージなど）
            return "users/editForm";
        }
    }

    // DELETE
    @PostMapping("/{username}/delete")
    public String delete(@PathVariable String username) {
        userService.delete(username);
        return "redirect:/users"; // ユーザーが削除された後にユーザー一覧ページにリダイレクトする
    }

    // SEARCH
    @GetMapping("/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        if (StringUtils.hasText(keyword)) {
            List<User> searchResults = userService.searchUsers(keyword);
            model.addAttribute("userList", searchResults);
        } else {
            model.addAttribute("userList", userService.findAll());
        }
        return "users/List";
    }
}