package com.example.its.web.issue;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.IOException;

@Data
public class IssueForm {

    @NotBlank
    @Size(max = 255)
    private String summary;

    @NotBlank
    @Size(max = 255)
    private String description;

    private MultipartFile image; // 画像のファイルを保持するフィールド

    // getImageBytes メソッドを追加して、MultipartFile をバイト配列に変換する
    public byte[] getImageBytes() throws IOException {
        if (image != null && !image.isEmpty()) {
            return image.getBytes();
        }
        return null;
    }
}