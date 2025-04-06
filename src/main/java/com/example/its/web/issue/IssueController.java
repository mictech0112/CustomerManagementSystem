package com.example.its.web.issue;

import com.example.its.domain.auth.CustomUserDetails;
import com.example.its.domain.issue.IssueEntity;
import com.example.its.domain.issue.IssueService;
import com.example.its.service.CsvExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.its.domain.auth.User;
import com.example.its.domain.auth.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final CsvExportService csvExportService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @GetMapping
    public String showList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long userId = getUserId(authentication);
        model.addAttribute("issueList", issueService.findAll(userId));
        return "issues/list";
    }

    private long getUserId(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                return ((CustomUserDetails) principal).getId();
            }
        }
        return 0;
    }

    @GetMapping("/creationForm")
    public String showCreationForm(Model model) {
        model.addAttribute("issueForm", new IssueForm());
        return "issues/creationForm";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute IssueForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "issues/creationForm";
        }

        try {
            issueService.create(form.getSummary(), form.getDescription(), form.getImageBytes());
            return "redirect:/issues";
        } catch (IOException e) {
            return "issues/creationForm";
        }
    }

    @PostMapping("/like/{issueId}")
    public String addLike(@PathVariable("issueId") long issueId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                User user = userService.findByUsername(username);
                issueService.likeIssue(user.getId(), issueId);
            }
        }
        return "redirect:/issues";
    }

    @PostMapping("/unlike/{issueId}")
    public String removeLike(@PathVariable("issueId") long issueId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                User user = userService.findByUsername(username);
                issueService.unlikeIssue(user.getId(), issueId);
            }
        }
        return "redirect:/issues";
    }

    @GetMapping("/{issueId}")
    public String showDetail(@PathVariable("issueId") long issueId, Model model, Authentication authentication) {
        IssueEntity issue = issueService.findById(issueId);
        model.addAttribute("issue", issue);

        // ログインユーザーの権限を確認し、ADMINの場合のみエクスポートリンクを表示
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"))) {
            model.addAttribute("isAdmin", true);
        }

        return "issues/detail";
    }

    @GetMapping("/{issueId}/export")
    public void exportToCsv(@PathVariable("issueId") long issueId, HttpServletResponse response) {
        IssueEntity issue = issueService.findById(issueId);
        if (issue != null) {
            try {
                csvExportService.exportIssueToCsv(issue, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/{issueId}/image")
    public void showIssueImage(@PathVariable("issueId") long issueId, HttpServletResponse response) {
        IssueEntity issue = issueService.findById(issueId);
        if (issue != null && issue.getImage() != null) {
            byte[] imageData = issue.getImage();
            response.setContentType("image/jpeg");
            response.setContentLength(imageData.length);
            try {
                response.getOutputStream().write(imageData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}