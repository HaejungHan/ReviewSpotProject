package com.sparta.reviewspotproject.mvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.reviewspotproject.controller.CommentController;
import com.sparta.reviewspotproject.dto.CommentRequestDto;
import com.sparta.reviewspotproject.dto.CommentResponseDto;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.security.UserDetailsImpl;
import com.sparta.reviewspotproject.service.CommentService;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(CommentController.class)
class CommentControllerMvcTest {

  @MockBean
  CommentService commentService;

  @InjectMocks
  private CommentController commentController;

  @Autowired
  private MockMvc mvc;

  private Principal mockPrincipal;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private ObjectMapper objectMapper;

  private UserDetailsImpl mockUserDetailsImpl;

  @BeforeEach
  public void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity(new MockSpringSecurityFilter()))
        .build();
  }

  private void mockUserSetup() {
    // Mock 테스트 유져 생성
    String userId = "hhanni12345";
    String password = "@hhanni1234567";
    String userName = "하니와요니";
    String email = "hhanni0705@gmail.com";
    UserStatus userStatus = UserStatus.MEMBER;
    User testUser = new User(userId, userName, password, email, userStatus);
    UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
    mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "",
        testUserDetails.getAuthorities());
  }

  @Test
  @DisplayName("댓글 작성 성공 테스트")
  public void createComment_Success() throws Exception {
    // given
    this.mockUserSetup();

    Long postId = 1L;

    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("댓글 내용");

    CommentResponseDto responseDto = new CommentResponseDto();
    responseDto.setId(11L);
    responseDto.setContents(requestDto.getContents());
    responseDto.setCreatedAt(LocalDateTime.now());
    responseDto.setModifiedAt(LocalDateTime.now());

    when(commentService.createComment(ArgumentMatchers.eq(postId),ArgumentMatchers.any(CommentRequestDto.class),
        ArgumentMatchers.any(User.class)))
        .thenReturn(responseDto);

    // when - then
    mvc.perform(post("/api/comment/{postId}", postId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.postId").value(1L))
            .andExpect(jsonPath("$.id").value(11L))
            .andExpect(jsonPath("$.contents").value(requestDto.getContents()));

  }

  @Test
  @DisplayName("댓글 작성 실패 테스트 - 내용을 입력하지 않은경우")
  public void createComment_Fail() throws Exception {
    // given
    this.mockUserSetup();

    Long postId = 1L;

    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("");

    CommentResponseDto responseDto = new CommentResponseDto();
    responseDto.setContents(requestDto.getContents());


    when(commentService.createComment(ArgumentMatchers.eq(postId),ArgumentMatchers.any(CommentRequestDto.class),
        ArgumentMatchers.any(User.class)))
        .thenReturn(responseDto);

    // when - then
    mvc.perform(post("/api/comment/{postId}", postId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().isBadRequest());

  }

  @Test
  @DisplayName("댓글 작성 실패 테스트2 - postId를 입력하지 않은 경우")
  public void createComment_Fail_2() throws Exception {
    // given
    this.mockUserSetup();
    Long postId = null;

    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("댓글의 내용");

    CommentResponseDto responseDto = new CommentResponseDto();
    responseDto.setContents(requestDto.getContents());

    // when - then
    mvc.perform(post("/api/comment/{postId}", postId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().is4xxClientError());
  }

  @Test
  @DisplayName("댓글 수정 성공 테스트")
  public void updateComment_Success() throws Exception {
    // given
    this.mockUserSetup();

    Long commentId = 11L;

    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("댓글 내용 수정");

    CommentResponseDto responseDto = new CommentResponseDto();
    responseDto.setId(commentId);
    responseDto.setContents(requestDto.getContents());
    responseDto.setCreatedAt(LocalDateTime.now());
    responseDto.setModifiedAt(LocalDateTime.now());

    when(commentService.updateComment(ArgumentMatchers.eq(commentId), ArgumentMatchers.any(CommentRequestDto.class),
        ArgumentMatchers.any(User.class)))
        .thenReturn(responseDto);

    // when - then
    mvc.perform(put("/api/comment/{commentId}", commentId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(commentId))
            .andExpect(jsonPath("$.contents").value(requestDto.getContents()));
  }

  @Test
  @DisplayName("댓글 수정 실패1 테스트 - commentId 입력하지 않은 경우")
  public void updatePost_Fail_1() throws Exception {
    // given
    this.mockUserSetup();

    Long commentId = null;

    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("댓글 내용 수정");

    CommentResponseDto responseDto = new CommentResponseDto();
    responseDto.setId(commentId);
    responseDto.setContents(requestDto.getContents());
    responseDto.setCreatedAt(LocalDateTime.now());
    responseDto.setModifiedAt(LocalDateTime.now());

    when(commentService.updateComment(ArgumentMatchers.eq(commentId), ArgumentMatchers.any(CommentRequestDto.class),
        ArgumentMatchers.any(User.class)))
        .thenReturn(responseDto);

    // when - then
    mvc.perform(put("/api/comment/{commentId}", commentId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().is4xxClientError());
  }

  @Test
  @DisplayName("댓글 수정 실패2 테스트 - 수정 내용 미입력한 경우")
  public void updatePost_Fail_2() throws Exception {
    // given
    this.mockUserSetup();

    Long commentId = 1L;

    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("");

    CommentResponseDto responseDto = new CommentResponseDto();
    responseDto.setId(commentId);
    responseDto.setContents(requestDto.getContents());
    responseDto.setCreatedAt(LocalDateTime.now());
    responseDto.setModifiedAt(LocalDateTime.now());

    when(commentService.updateComment(ArgumentMatchers.eq(commentId), ArgumentMatchers.any(CommentRequestDto.class),
        ArgumentMatchers.any(User.class)))
        .thenReturn(responseDto);

    // when - then
    mvc.perform(put("/api/comment/{commentId}", commentId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("댓글 삭제 성공 테스트")
  public void deletePost_Success() throws Exception {
    // given
    this.mockUserSetup();

    Long commentId = 1L;

    doNothing().when(commentService).deleteComment(ArgumentMatchers.eq(commentId), ArgumentMatchers.any(User.class));

    // when - then
    mvc.perform(delete("/api/comment/{commentId}", commentId)
            .contentType(MediaType.APPLICATION_JSON)
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("댓글이 성공적으로 삭제되었습니다."));
  }

  @Test
  @DisplayName("댓글 삭제 실패 테스트 - commentId를 입력하지 않은 경우")
  public void deletePost_Fail() throws Exception {
    // given
    this.mockUserSetup();

    Long commentId = null;

    doNothing().when(commentService).deleteComment(ArgumentMatchers.eq(commentId), ArgumentMatchers.any(User.class));

    // when - then
    mvc.perform(delete("/api/comment/{commentId}", commentId)
            .contentType(MediaType.APPLICATION_JSON)
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().is4xxClientError());
  }

  @Test
  @DisplayName("댓글 전체조회 성공 테스트")
  public void getPosts_Success() throws Exception {
    // given
    Long postId = 1L;

    CommentResponseDto responseDto1 = new CommentResponseDto();
    responseDto1.setId(11L);
    responseDto1.setContents("게시글 내용1");
    responseDto1.setCreatedAt(LocalDateTime.now());
    responseDto1.setModifiedAt(LocalDateTime.now());

    CommentResponseDto responseDto2 = new CommentResponseDto();
    responseDto2.setId(12L);
    responseDto2.setContents("게시글 내용2");
    responseDto2.setCreatedAt(LocalDateTime.now());
    responseDto2.setModifiedAt(LocalDateTime.now());

    List<CommentResponseDto> commentResponseDtoList = Arrays.asList(responseDto1, responseDto2);

    when(commentService.getAllComment(postId)).thenReturn(commentResponseDtoList);

    // when - then
    mvc.perform(get("/api/comments/{postId}", postId)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].id").value(11L))
            .andExpect(jsonPath("$[1].id").value(12L));
  }
}

