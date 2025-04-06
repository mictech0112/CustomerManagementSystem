package com.example.its.domain.issue;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface IssueRepository {

    @Select("SELECT i.id, i.summary, i.description, i.image, COALESCE(uil.user_id, 0) AS liked " +
            "FROM issues i " +
            "LEFT JOIN user_issue_likes uil ON i.id = uil.issue_id AND uil.user_id = #{userId} " +
            "WHERE i.deleted_at IS NULL") // 論理削除されていないレコードのみ取得
    List<IssueEntity> findAll(@Param("userId") long userId);

    @Insert("INSERT INTO user_issue_likes (user_id, issue_id) VALUES (#{userId}, #{issueId})")
    void likeIssue(@Param("userId") long userId, @Param("issueId") long issueId);

    @Delete("DELETE FROM user_issue_likes WHERE user_id = #{userId} AND issue_id = #{issueId}")
    void unlikeIssue(@Param("userId") long userId, @Param("issueId") long issueId);

    // image フィールドを byte[] に変更し、SQLタイプを BLOB に変更する
    @Insert("INSERT INTO issues (summary, description, image) VALUES (#{summary}, #{description}, #{image, jdbcType=BLOB})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(IssueEntity issue);

    @Select("SELECT id, summary, description, liked, image FROM issues WHERE id = #{issueId}")
    IssueEntity findById(@Param("issueId") long issueId);

    @Update("UPDATE issues SET liked = #{liked} WHERE id = #{issueId}")
    void updateLiked(@Param("liked") boolean liked, @Param("issueId") long issueId);
}