package com.sparta.reviewspotproject.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sparta.reviewspotproject.dto.PostRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PostTest {

  @Test
  @DisplayName("게시물 entity Test")
  public void entityTest () {
    // given
    User user = new User();

    PostRequestDto requestDto = new PostRequestDto("게시물의 제목입니다.","게시물의 내용입니다.");

    // when
    Post post = new Post(requestDto, user);

    // then
    assertEquals("게시물의 제목입니다.", post.getTitle());
    assertEquals("게시물의 내용입니다.", post.getContents());
    assertEquals(user, post.getUser());
    assertEquals(0, post.getPostLikesCount());

  }

  @Test
  @DisplayName("게시물 update 메서드 테스트")
  public void updateTest() {
    // given
    User user = new User();

    PostRequestDto updateRequestDto = new PostRequestDto("게시물의 제목을 변경합니다.", "게시물의 내용을 변경합니다.");

    // when
    PostRequestDto requestDto = new PostRequestDto("원래의 게시물 제목 입니다.", "원래의 게시물 내용 입니다.");
    Post originalPost = new Post(requestDto, user);

    originalPost.update(updateRequestDto);

    // then
    assertEquals("게시물의 제목을 변경합니다.", originalPost.getTitle());
    assertEquals("게시물의 내용을 변경합니다.", originalPost.getContents());

  }

}
