package com.sparta.reviewspotproject.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sparta.reviewspotproject.dto.CommentRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommentTest {

  @Test
  @DisplayName("댓글 entity Test")
  public void entityTest() {
    // given
    User user = new User();

    Post post = new Post();

    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("댓글의 내용입니다.");

    // when
    Comment comment = new Comment(requestDto, post, user);

    // then
    assertEquals("댓글의 내용입니다.", comment.getContents());
    assertEquals(user, comment.getUser());
    assertEquals(post.getId(), comment.getPost().getId());
    assertEquals(0, comment.getCommentLikesCount());

  }

  @Test
  @DisplayName("댓글 update 메서드 테스트")
  public void updateTest() {
    // given
    User user = new User();

    Post post = new Post();

    CommentRequestDto updateRequestDto = new CommentRequestDto();
    updateRequestDto.setContents("댓글의 내용을 변경합니다.");

    // when
    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("원래의 댓글 내용입니다.");
    Comment originalComment = new Comment(requestDto, post, user);

    originalComment.update(updateRequestDto);

    // then
    assertEquals("댓글의 내용을 변경합니다.", originalComment.getContents());

  }
}
