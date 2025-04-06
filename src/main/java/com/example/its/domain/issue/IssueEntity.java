package com.example.its.domain.issue;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IssueEntity {

    private long id;
    private String summary;
    private String description;
    private boolean liked;
    private byte[] image; // バイナリデータとして画像を保持するフィールド
    private LocalDateTime deletedAt; // 論理削除用のフィールドを追加
}