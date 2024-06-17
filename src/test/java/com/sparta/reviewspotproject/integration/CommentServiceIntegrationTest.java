package com.sparta.reviewspotproject.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sparta.reviewspotproject.dto.CommentRequestDto;
import com.sparta.reviewspotproject.dto.CommentResponseDto;
import com.sparta.reviewspotproject.entity.Post;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.repository.CommentRepository;
import com.sparta.reviewspotproject.repository.PostRepository;
import com.sparta.reviewspotproject.repository.UserRepository;
import com.sparta.reviewspotproject.service.CommentService;
import com.sparta.reviewspotproject.service.PostService;
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
@Transactional
public class CommentServiceIntegrationTest {

  @Autowired
  private CommentService commentService;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PostService postService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private User currentUser;
  private Post testPost;

  @BeforeEach
  public void setUpTestUserAndTestPost() {
    currentUser = new User();
    currentUser.setUserId("abcdefg12345");
    currentUser.setPassword(passwordEncoder.encode("password@1234"));
    currentUser.setUserName("하니와요니");
    currentUser.setEmail("user@gmail.com");
    currentUser.setUserStatus(UserStatus.MEMBER);
    userRepository.save(currentUser);

    testPost = new Post();
    testPost.setTitle("게시물 제목");
    testPost.setContents("게시물 내용");
    postRepository.save(testPost);
  }

  @AfterEach
  public void cleanUpTestUserAndTestPost() {
    userRepository.delete(currentUser);
    postRepository.delete(testPost);
  }

  @Test
  @DisplayName("댓글 작성 통합 테스트")
  public void createComment() {
    // given
    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("댓글 내용");

    // when
    CommentResponseDto responseDto = commentService.createComment(testPost.getId(), requestDto, currentUser);

    // then
    assertEquals("댓글 내용", responseDto.getContents());
  }

  @Test
  @DisplayName("댓글 수정 통합 테스트")
  public void updateComment() {
    // given
    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("댓글 내용");
    CommentResponseDto createComment = commentService.createComment(testPost.getId(), requestDto, currentUser);

    CommentRequestDto updateRequest = new CommentRequestDto();
    updateRequest.setContents("댓글 내용 수정");

    // when
    CommentResponseDto updateComment = commentService.updateComment(createComment.getId(), updateRequest, currentUser);

    // then
    assertEquals("댓글 내용 수정", updateComment.getContents());
  }

  @Test
  @DisplayName("댓글 수정 통합테스트 - 게시물 작성자와 다를 때")
  public void updateComment_Fail() {
    // given
    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("댓글 내용");
    CommentResponseDto createComment = commentService.createComment(testPost.getId(), requestDto, currentUser);

    CommentRequestDto updateRequest = new CommentRequestDto();
    updateRequest.setContents("댓글 내용 수정");

    User notAuthUser = new User();
    notAuthUser.setUserId("IamNotAuthUser1234");
    notAuthUser.setPassword(passwordEncoder.encode("password@1234"));
    notAuthUser.setUserName("IamNotAuthUser");
    notAuthUser.setEmail("user11@gmail.com");
    notAuthUser.setUserStatus(UserStatus.MEMBER);
    userRepository.save(notAuthUser);

    // when - then
    assertThrows(IllegalArgumentException.class, () ->
        commentService.updateComment(createComment.getId(), updateRequest, notAuthUser));

    userRepository.delete(notAuthUser);
  }

  @Test
  @DisplayName("댓글 삭제 통합테스트")
  public void deleteComment() {
    // given
    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("댓글 내용");
    CommentResponseDto createComment = commentService.createComment(testPost.getId(), requestDto, currentUser);

    // when
    commentService.deleteComment(createComment.getId(), currentUser);

    // then
    assertFalse(commentRepository.existsById(createComment.getId()));
  }

  @Test
  @DisplayName("댓글 삭제 통합 실패테스트 - 댓글 작성자가 다를 때")
  public void deleteComment_Fail() {
    // given
    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("댓글 내용");
    CommentResponseDto createComment = commentService.createComment(testPost.getId(), requestDto, currentUser);

    User notAuthUser = new User();
    notAuthUser.setUserId("IamNotAuthUser1234");
    notAuthUser.setPassword(passwordEncoder.encode("password@1234"));
    notAuthUser.setUserName("IamNotAuthUser");
    notAuthUser.setEmail("user11@gmail.com");
    notAuthUser.setUserStatus(UserStatus.MEMBER);
    userRepository.save(notAuthUser);

    // when - then
    assertThrows(IllegalArgumentException.class, () -> postService.deletePost(createComment.getId(), notAuthUser));

    userRepository.delete(notAuthUser);
  }

  @Test
  @DisplayName("선택 게시물의 댓글 전체 조회 통합 테스트")
  public void getAllCommentsByPostId() {
    // given
    commentRepository.deleteAll();
    CommentRequestDto requestDto1 = new CommentRequestDto();
    requestDto1.setContents("댓글 내용1");
    CommentRequestDto requestDto2 = new CommentRequestDto();
    requestDto2.setContents("댓글 내용2");

    commentService.createComment(testPost.getId(), requestDto1, currentUser);
    commentService.createComment(testPost.getId(), requestDto2, currentUser);

    // when
    List<CommentResponseDto> getAllComment = commentService.getAllComment(testPost.getId());

    // then
    assertEquals(2, getAllComment.size());
  }

}
