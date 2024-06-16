package com.sparta.reviewspotproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.reviewspotproject.dto.PostRequestDto;
import com.sparta.reviewspotproject.dto.PostResponseDto;
import com.sparta.reviewspotproject.entity.Post;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.repository.PostRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

  @Mock
  PostRepository postRepository;

  @InjectMocks
  PostService postService;

  private User mockTestUser;

  @BeforeEach
  public void mockTestUserSetUp() {
    String userId = "hhanni12345";
    String password = "@hhanni1234567";
    String userName = "하니와요니";
    String email = "hhanni0705@gmail.com";
    UserStatus userStatus = UserStatus.MEMBER;
    mockTestUser = new User(userId, userName, password, email, userStatus);
  }

  @Test
  @DisplayName("게시물 작성 테스트")
  public void createPost() {
    // given
    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("게시물 제목");
    requestDto.setContents("게시물 내용");

    Post post = new Post();
    post.setId(1L);
    post.setTitle(requestDto.getTitle());
    post.setContents(requestDto.getContents());
    post.setUser(mockTestUser);

    when(postRepository.save(any(Post.class))).thenReturn(post);

    // when
    PostResponseDto responseDto = postService.createPost(requestDto, mockTestUser);

    // then
    assertNotNull(responseDto);
    assertEquals(post.getId(), responseDto.getPostId());
    assertEquals(post.getTitle(), responseDto.getTitle());
    assertEquals(post.getContents(), responseDto.getContents());

    verify(postRepository).save(any(Post.class));
  }

  @Test
  @DisplayName("선택 게시물 조회 테스트")
  public void getPost() {
    // given
    Long postId = 1L;
    Post post = new Post();
    post.setId(postId);
    post.setTitle("게시물 제목");
    post.setContents("게시물 내용");

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    // when
    PostResponseDto responseDto = postService.getPost(postId);

    // then
    assertNotNull(responseDto);
    assertEquals(post.getId(), responseDto.getPostId());
    assertEquals(post.getTitle(), responseDto.getTitle());
    assertEquals(post.getContents(), responseDto.getContents());

    verify(postRepository).findById(postId);
  }

  @Test
  @DisplayName("게시물 전체 조회 테스트")
  public void getPosts() {
    // given
    Post post1 = new Post();
    post1.setTitle("게시물 제목1");
    post1.setContents("게시물 내용1");

    Post post2 = new Post();
    post2.setTitle("게시물 제목2");
    post2.setContents("게시물 내용2");

    when(postRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Arrays.asList(post1, post2));

    // when
    List<PostResponseDto> responseDtoList = postService.getPosts();

    // then
    assertNotNull(responseDtoList);
    assertEquals(2, responseDtoList.size());

    verify(postRepository).findAllByOrderByCreatedAtDesc();
  }

  @Test
  @DisplayName("게시물 수정 테스트")
  public void updatePost() {
    // given
    Long postId = 1L;

    Post post = new Post();
    post.setId(postId);
    post.setUser(mockTestUser);

    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("수정 게시물 제목");
    requestDto.setContents("수정 게시물 내용");

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    // when
    PostResponseDto responseDto = postService.updatePost(postId, requestDto, mockTestUser);

    // then
    assertNotNull(responseDto);
    assertEquals(post.getId(), responseDto.getPostId());
    assertEquals(requestDto.getTitle(), responseDto.getTitle());
    assertEquals(requestDto.getContents(), responseDto.getContents());

    verify(postRepository).findById(postId);
    verify(postRepository).save(any(Post.class));
  }

  @Test
  @DisplayName("게시물 수정 실패 테스트 - 게시물 작성자가 아닐때")
  public void updatePost_Fail_1() {
    // given
    Long postId = 1L;
    Post post = new Post();
    post.setId(postId);
    post.setTitle("1");
    post.setContents("2");
    post.setUser(mockTestUser);

    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("수정 게시물 제목");
    requestDto.setContents("수정 게시물 내용");

    User notAuthUser = new User();
    notAuthUser.setId(22L);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    // when-then
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
      postService.updatePost(postId, requestDto, notAuthUser);
    });

    verify(postRepository, never()).save(any(Post.class));
    assertEquals("게시물 작성자가 아니므로 수정할 수 없습니다.", exception.getMessage());
  }

  @Test
  @DisplayName("게시물 삭제 성공 테스트")
  public void deletePost() {
    // given
    Long postId = 1L;
    Post post = new Post();
    post.setId(postId);
    post.setTitle("게시물 제목");
    post.setContents("게시물 내용");
    post.setUser(mockTestUser);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    // when
    postService.deletePost(postId, mockTestUser);

    // then
    verify(postRepository).delete(post);
  }

  @Test
  @DisplayName("게시물 삭제 실패 테스트 - 게시물 작성자가 아닐때")
  public void deletePost_Fail_1() {
    // given
    Long postId = 1L;
    Post post = new Post();
    post.setId(postId);
    post.setUser(mockTestUser);

    User notAuthUser = new User();
    notAuthUser.setId(22L);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    // when
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
      postService.deletePost(postId, notAuthUser);
    });

    // then
    verify(postRepository, never()).delete(post);
    assertEquals("게시물 작성자가 아니므로 삭제할 수 없습니다.", exception.getMessage());
  }

  @Test
  @DisplayName("게시물 찾기 메서드 성공 테스트")
  public void findPost_Success() {
    // given
    Long postId = 1L;
    Post post = new Post();
    post.setId(postId);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    // when
    Post findPost = postService.findPost(postId);

    // then
    assertNotNull(findPost);
    assertEquals(post.getId(), findPost.getId());
  }

  @Test
  @DisplayName("게시물 찾기 메서드 실패 테스트")
  public void findPost_Fail() {
    // given
    Long postId = 1L;

    when(postRepository.findById(postId)).thenReturn(Optional.empty());

    // when
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
      postService.findPost(postId);
    });

    // then
    assertEquals("게시물을 찾을 수 없습니다.", exception.getMessage());
  }

}
