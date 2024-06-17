package com.sparta.reviewspotproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.reviewspotproject.dto.CommentRequestDto;
import com.sparta.reviewspotproject.dto.CommentResponseDto;
import com.sparta.reviewspotproject.entity.Comment;
import com.sparta.reviewspotproject.entity.Post;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.repository.CommentRepository;
import com.sparta.reviewspotproject.repository.PostRepository;
import java.util.ArrayList;
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
public class CommentServiceTest {

  @Mock
  CommentRepository commentRepository;

  @Mock
  PostRepository postRepository;

  @InjectMocks
  CommentService commentService;

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
  @DisplayName("댓글 작성 테스트")
  public void createComment() {
    // given
    Long postId = 1L;
    Post post = new Post();
    post.setId(1L);

    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("댓글 내용");

    Comment comment = new Comment();
    comment.setId(1L);
    comment.setContents(requestDto.getContents());
    comment.setUser(mockTestUser);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));
    when(commentRepository.save(any(Comment.class))).thenReturn(comment);

    // when
    CommentResponseDto responseDto = commentService.createComment(postId, requestDto, mockTestUser);

    // then
    assertNotNull(responseDto);
    assertEquals(comment.getId(), responseDto.getId());
    assertEquals(comment.getContents(), responseDto.getContents());

    verify(postRepository).findById(postId);
    verify(commentRepository).save(any(Comment.class));
  }

  @Test
  @DisplayName("댓글 수정 테스트")
  public void updateComment() {
    // given
    Long commentId = 1L;

    Comment comment = new Comment();
    comment.setId(commentId);
    comment.setUser(mockTestUser);

    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("수정 댓글 내용");

    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

    // when
    CommentResponseDto responseDto = commentService.updateComment(commentId, requestDto, mockTestUser);

    // then
    assertNotNull(responseDto);
    assertEquals(comment.getId(), responseDto.getId());
    assertEquals(requestDto.getContents(), responseDto.getContents());

    verify(commentRepository).findById(commentId);
  }

  @Test
  @DisplayName("댓글 수정 실패 테스트 - 댓글 작성자가 아닐때")
  public void updateComment_Fail() {
    // given
    Long commentId = 1L;
    Comment comment = new Comment();
    comment.setId(commentId);
    comment.setUser(mockTestUser);

    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("수정 댓글 내용");

    User notAuthUser = new User();
    notAuthUser.setId(11L);

    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

    // when - then
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
      commentService.updateComment(commentId, requestDto, notAuthUser);
    });

    assertEquals("본인이 작성한 댓글만 수정할 수 있습니다.", exception.getMessage());
  }

  @Test
  @DisplayName("댓글 삭제 성공 테스트")
  public void deleteComment() {
    // given
    Long commentId = 1L;
    Comment comment = new Comment();
    comment.setId(commentId);
    comment.setUser(mockTestUser);

    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

    // when
    commentService.deleteComment(commentId, mockTestUser);

    // then
    verify(commentRepository).delete(comment);
  }

  @Test
  @DisplayName("댓글 삭제 실패 테스트 - 댓글 작성자가 아닐때")
  public void deleteComment_Fail() {
    // given
    Long commentId = 1L;
    Comment comment = new Comment();
    comment.setId(commentId);
    comment.setUser(mockTestUser);

    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

    User notAuthUser = new User();
    notAuthUser.setId(11L);

    // when
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
      commentService.deleteComment(commentId, notAuthUser);
    });

    // then
    verify(commentRepository, never()).delete(comment);
    assertEquals("본인이 작성한 댓글만 삭제할 수 있습니다.", exception.getMessage());
  }

  @Test
  @DisplayName("댓글 전체 조회 테스트")
  public void getAllComment() {
    // given
    Long postId = 1L;

   Post post = new Post();
   post.setId(postId);

   List<Comment> comments = new ArrayList<>();
   Comment comment1 = new Comment();
   comment1.setPost(post);
   Comment comment2 = new Comment();
   comment2.setPost(post);
    Comment comment3 = new Comment();
    comment3.setPost(post);
   comments.add(comment1);
   comments.add(comment2);
   comments.add(comment3);

   when(commentRepository.findByPostId(postId)).thenReturn(comments);

    // when
    List<CommentResponseDto> responseDtoList = commentService.getAllComment(postId);

    // then
    assertNotNull(responseDtoList);
    assertEquals(3, responseDtoList.size());

    verify(commentRepository).findByPostId(postId);
  }

  @Test
  @DisplayName("댓글 찾기 메서드 성공 테스트")
  public void findComment_Success() {
    // given
    Long commentId = 1L;
    
    Comment comment = new Comment();
    comment.setId(commentId);

    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

    // when
    Comment findComment = commentService.findCommentById(commentId);

    // then
    assertNotNull(findComment);
    assertEquals(comment.getId(), findComment.getId());

    verify(commentRepository).findById(commentId);
  }

  @Test
  @DisplayName("댓글 찾기 메서드 실패 테스트")
  public void findComment_Fail() {
    // given
    Long commentId = 1L;

    when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

    // when
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
      commentService.findCommentById(commentId);
    });

    // then
    assertEquals("해당 댓글을 찾을 수 없습니다.", exception.getMessage());
  }

}
