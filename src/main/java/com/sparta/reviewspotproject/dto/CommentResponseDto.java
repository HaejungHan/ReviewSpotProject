package com.sparta.reviewspotproject.dto;

import com.sparta.reviewspotproject.entity.Comment;
import com.sparta.reviewspotproject.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long postId;
    private Long id;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int commentLikesCount;

    public CommentResponseDto(Post post, Comment comment) {
        this.postId = post.getId();
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.commentLikesCount = comment.getCommentLikesCount();
    }

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.commentLikesCount = comment.getCommentLikesCount();
    }
}