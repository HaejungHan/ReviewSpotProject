package com.sparta.reviewspotproject.dto;

import com.sparta.reviewspotproject.entity.Comment;
import com.sparta.reviewspotproject.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int commentLikesCount;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.commentLikesCount = comment.getCommentLikesCount();
    }
}