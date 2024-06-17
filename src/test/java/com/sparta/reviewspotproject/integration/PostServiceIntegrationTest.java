package com.sparta.reviewspotproject.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sparta.reviewspotproject.dto.PostRequestDto;
import com.sparta.reviewspotproject.dto.PostResponseDto;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.repository.PostRepository;
import com.sparta.reviewspotproject.repository.UserRepository;
import com.sparta.reviewspotproject.service.PostService;
import com.sparta.reviewspotproject.service.UserService;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class PostServiceIntegrationTest {
  @Autowired
  private UserService userService;

  @Autowired
  private PostService postService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private PostRepository postRepository;

  private User currentUser;

  @BeforeEach
  public void setUpTestUser() {
    currentUser = new User();
    currentUser.setUserId("12345");
    currentUser.setPassword(passwordEncoder.encode("password@1234"));
    currentUser.setUserName("하니와요니");
    currentUser.setEmail("user@gmail.com");
    currentUser.setUserStatus(UserStatus.MEMBER);
    userRepository.save(currentUser);
  }

  @AfterEach
  @Transactional
  public void cleanUpTestUser() {
    userRepository.delete(currentUser);
  }

  @Test
  @Transactional
  @DisplayName("게시물 작성 통합 테스트")
  public void createPost() {
    // given
    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("새로운 게시물 제목");
    requestDto.setContents("새로운 게시물 내용");

    // when
    PostResponseDto responseDto = postService.createPost(requestDto, currentUser);

    // then
    assertEquals("새로운 게시물 제목", responseDto.getTitle());
    assertEquals("새로운 게시물 내용", responseDto.getContents());
  }

  @Test
  @Transactional
  @DisplayName("선택 게시물 조회 통합 테스트")
  public void getPost() {
    // given
    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("새로운 게시물 제목");
    requestDto.setContents("새로운 게시물 내용");

    PostResponseDto createPost = postService.createPost(requestDto, currentUser);

    // when
    PostResponseDto getPost = postService.getPost(createPost.getPostId());

    // then
    assertEquals(createPost.getPostId(), getPost.getPostId());
    assertEquals(createPost.getTitle(), getPost.getTitle());
  }

  @Test
  @Transactional
  @DisplayName("게시물 수정 통합 테스트")
  public void updatePost() {
    // given
    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("게시물 제목");
    requestDto.setContents("게시물 내용");
    PostResponseDto createPost = postService.createPost(requestDto, currentUser);

    PostRequestDto updateRequest = new PostRequestDto();
    updateRequest.setTitle("게시물 제목 수정!!");
    updateRequest.setContents("게시물 내용 수정!!");

    // when
    PostResponseDto updatePost = postService.updatePost(createPost.getPostId(), updateRequest, currentUser);

    // then
    assertEquals(createPost.getPostId(), updatePost.getPostId());
    assertEquals(updateRequest.getTitle(), updatePost.getTitle());
  }

  @Test
  @Transactional
  @DisplayName("게시물 수정 통합 실패테스트 - 게시물 작성자와 다를때")
  public void updatePost_Fail() {
    // given
    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("원래의 게시물 제목");
    requestDto.setContents("원래의 게시물 내용");
    PostResponseDto createPost = postService.createPost(requestDto, currentUser);

    PostRequestDto updateRequest = new PostRequestDto();
    updateRequest.setTitle("게시물 제목 수정!!");
    updateRequest.setContents("게시물 내용 수정!!");

    User notAuthUser = new User();
    notAuthUser.setUserId("IamNotAuthUser1234");
    notAuthUser.setPassword(passwordEncoder.encode("password@1234"));
    notAuthUser.setUserName("IamNotAuthUser");
    notAuthUser.setEmail("user11@gmail.com");
    notAuthUser.setUserStatus(UserStatus.MEMBER);
    userRepository.save(notAuthUser);

    // when - then
    assertThrows(IllegalArgumentException.class, () -> postService.updatePost(createPost.getPostId(), updateRequest, notAuthUser));

    userRepository.delete(notAuthUser);
  }

  @Test
  @Transactional
  @DisplayName("게시물 삭제 통합테스트")
  public void deletePost() {
    // given
    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("원래의 게시물 제목");
    requestDto.setContents("원래의 게시물 내용");
    PostResponseDto createPost = postService.createPost(requestDto, currentUser);

    // when
    postService.deletePost(createPost.getPostId(), currentUser);

    // then
    assertFalse(postRepository.existsById(createPost.getPostId()));

  }

  @Test
  @Transactional
  @DisplayName("게시물 삭제 통합 실패테스트 - 게시물 작성자와 다를때")
  public void deletePost_Fail() {
    // given
    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("원래의 게시물 제목");
    requestDto.setContents("원래의 게시물 내용");
    PostResponseDto createPost = postService.createPost(requestDto, currentUser);

    User notAuthUser = new User();
    notAuthUser.setUserId("IamNotAuthUser1234");
    notAuthUser.setPassword(passwordEncoder.encode("password@1234"));
    notAuthUser.setUserName("IamNotAuthUser");
    notAuthUser.setEmail("user11@gmail.com");
    notAuthUser.setUserStatus(UserStatus.MEMBER);
    userRepository.save(notAuthUser);

    // when - then
    assertThrows(IllegalArgumentException.class, () -> postService.deletePost(createPost.getPostId(), notAuthUser));

    userRepository.delete(notAuthUser);
  }

  @Test
  @Transactional
  @DisplayName("전체 게시물 조회 통합테스트")
  public void getPosts() {
    postRepository.deleteAll();
    // given
    PostRequestDto requestDto1 = new PostRequestDto();
    requestDto1.setTitle("게시물 제목1");
    requestDto1.setContents("게시물 내용1");
    PostRequestDto requestDto2 = new PostRequestDto();
    requestDto2.setTitle("게시물 제목2");
    requestDto2.setContents("게시물 내용2");

    postService.createPost(requestDto1, currentUser);
    postService.createPost(requestDto2, currentUser);

    // when
    List<PostResponseDto> getPosts = postService.getPosts();

    // then
    assertEquals(2, getPosts.size());
  }

}