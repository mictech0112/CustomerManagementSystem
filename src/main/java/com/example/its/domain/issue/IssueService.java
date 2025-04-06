package com.example.its.domain.issue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;

    public List<IssueEntity> findAll(long userId) {
        return issueRepository.findAll(userId);
    }

    @Transactional
    public void create(String summary, String description, byte[] image) throws IOException {
        IssueEntity issue = new IssueEntity();
        issue.setSummary(summary);
        issue.setDescription(description);
        issue.setImage(image);
        issueRepository.insert(issue);
    }

    public IssueEntity findById(long issueId) {
        return issueRepository.findById(issueId);
    }

    @Transactional
    public void likeIssue(long userId, Long issueId) {
        issueRepository.likeIssue(userId, issueId);
        issueRepository.updateLiked(true, issueId);
    }

    @Transactional
    public void unlikeIssue(long userId, Long issueId) {
        issueRepository.unlikeIssue(userId, issueId);
        issueRepository.updateLiked(false, issueId);
    }
}