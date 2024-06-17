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
import com.sparta.reviewspotproject.controller.PostController;
import com.sparta.reviewspotproject.dto.PostRequestDto;
import com.sparta.reviewspotproject.dto.PostResponseDto;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.security.UserDetailsImpl;
import com.sparta.reviewspotproject.service.PostService;
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

@WebMvcTest(PostController.class)
class PostControllerMvcTest {

  @MockBean
  PostService postService;

  @InjectMocks
  private PostController postController;

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
  @DisplayName("게시물 작성 성공 테스트")
  public void createPost_Success() throws Exception {
    // given
    this.mockUserSetup();
    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("게시글 제목");
    requestDto.setContents("게시글 내용");

    PostResponseDto responseDto = new PostResponseDto();
    responseDto.setPostId(1L);
    responseDto.setTitle(requestDto.getTitle());
    responseDto.setContents(requestDto.getContents());
    responseDto.setCreatedAt(LocalDateTime.now());
    responseDto.setModifiedAt(LocalDateTime.now());

    when(postService.createPost(ArgumentMatchers.any(PostRequestDto.class),
        ArgumentMatchers.any(User.class)))
        .thenReturn(responseDto);

    // when - then
    mvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.postId").value(1L))
            .andExpect(jsonPath("$.title").value(requestDto.getTitle()))
            .andExpect(jsonPath("$.contents").value(requestDto.getContents()));

  }

  @Test
  @DisplayName("게시물 작성 실패 테스트1 - 제목을 입력하지 않은경우")
  public void createPost_Fail_1() throws Exception {
    // given
    this.mockUserSetup();
    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("");
    requestDto.setContents("게시물 내용");

    PostResponseDto responseDto = new PostResponseDto();
    responseDto.setTitle(requestDto.getTitle());
    responseDto.setContents(requestDto.getContents());

    // when - then
    mvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("게시물 작성 실패 테스트2 - 내용을 입력하지 않은경우")
  public void createPost_Fail_2() throws Exception {
    // given
    this.mockUserSetup();
    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("게시물 제목");
    requestDto.setContents("");

    PostResponseDto responseDto = new PostResponseDto();
    responseDto.setTitle(requestDto.getTitle());
    responseDto.setContents(requestDto.getContents());

    // when - then
    mvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("선택 게시물 조회 성공 테스트")
  public void getPost_Success() throws Exception {
    // given
    Long postId = 1L;

    PostResponseDto responseDto = new PostResponseDto();
    responseDto.setPostId(postId);
    responseDto.setTitle("게시글 제목");
    responseDto.setContents("게시글 내용");
    responseDto.setCreatedAt(LocalDateTime.now());
    responseDto.setModifiedAt(LocalDateTime.now());

    when(postService.getPost(postId)).thenReturn(responseDto);

    // when - then
    mvc.perform(get("/api/posts/{postId}", postId)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.postId").value(postId))
            .andExpect(jsonPath("$.title").value("게시글 제목"))
            .andExpect(jsonPath("$.contents").value("게시글 내용"));
  }

  @Test
  @DisplayName("선택 게시글 조회 실패 테스트 - postId를 입력하지 않은 경우")
  public void getPost_Fail() throws Exception {
    // given
    Long postId = null;

    PostResponseDto responseDto = new PostResponseDto();
    responseDto.setTitle("게시글 제목");
    responseDto.setContents("게시글 내용");
    responseDto.setCreatedAt(LocalDateTime.now());
    responseDto.setModifiedAt(LocalDateTime.now());

    when(postService.getPost(postId)).thenReturn(responseDto);

    // when - then
    mvc.perform(get("/api/posts/{postId}", postId)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError());
  }

  @Test
  @DisplayName("게시글 전체조회 성공 테스트")
  public void getPosts_Success() throws Exception {
    // given
    PostResponseDto responseDto1 = new PostResponseDto();
    responseDto1.setPostId(1L);
    responseDto1.setTitle("게시글 제목1");
    responseDto1.setContents("게시글 내용1");
    responseDto1.setCreatedAt(LocalDateTime.now());
    responseDto1.setModifiedAt(LocalDateTime.now());

    PostResponseDto responseDto2 = new PostResponseDto();
    responseDto2.setPostId(2L);
    responseDto2.setTitle("게시글 제목2");
    responseDto2.setContents("게시글 내용2");
    responseDto2.setCreatedAt(LocalDateTime.now());
    responseDto2.setModifiedAt(LocalDateTime.now());

    List<PostResponseDto> postResponseDtoList = Arrays.asList(responseDto1, responseDto2);

    when(postService.getPosts()).thenReturn(postResponseDtoList);

    // when - then
    mvc.perform(get("/api/posts")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].postId").value(1L))
            .andExpect(jsonPath("$[1].postId").value(2L));
  }

  @Test
  @DisplayName("게시글 전체조회 테스트 - 게시글이 없는 경우")
  public void getPosts_NoPost() throws Exception {
    // given
    List<PostResponseDto> postResponseDtoList = Arrays.asList();

    when(postService.getPosts()).thenReturn(postResponseDtoList);

    // when - then
    mvc.perform(get("/api/posts")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("먼저 작성하여 소식을 알려보세요!"));
  }

  @Test
  @DisplayName("게시물 수정 성공 테스트")
  public void updatePost_Success() throws Exception {
    // given
    this.mockUserSetup();

    Long postId = 1L;

    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("게시글 제목 수정");
    requestDto.setContents("게시글 내용 수정");

    PostResponseDto responseDto = new PostResponseDto();
    responseDto.setPostId(postId);
    responseDto.setTitle(requestDto.getTitle());
    responseDto.setContents(requestDto.getContents());
    responseDto.setCreatedAt(LocalDateTime.now());
    responseDto.setModifiedAt(LocalDateTime.now());

    when(postService.updatePost(ArgumentMatchers.eq(postId), ArgumentMatchers.any(PostRequestDto.class),
        ArgumentMatchers.any(User.class)))
        .thenReturn(responseDto);

    // when - then
    mvc.perform(put("/api/posts/{postId}", postId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.postId").value(postId))
            .andExpect(jsonPath("$.title").value(requestDto.getTitle()))
            .andExpect(jsonPath("$.contents").value(requestDto.getContents()));
  }

  @Test
  @DisplayName("게시물 수정 실패1 테스트 - postId 입력하지 않은 경우")
  public void updatePost_Fail_1() throws Exception {
    // given
    this.mockUserSetup();

    Long postId = null;

    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("게시글 제목 수정");
    requestDto.setContents("게시글 내용 수정");

    PostResponseDto responseDto = new PostResponseDto();
    responseDto.setPostId(postId);
    responseDto.setTitle(requestDto.getTitle());
    responseDto.setContents(requestDto.getContents());
    responseDto.setCreatedAt(LocalDateTime.now());
    responseDto.setModifiedAt(LocalDateTime.now());

    when(postService.updatePost(ArgumentMatchers.eq(postId), ArgumentMatchers.any(PostRequestDto.class),
        ArgumentMatchers.any(User.class)))
        .thenReturn(responseDto);

    // when - then
    mvc.perform(put("/api/posts/{postId}", postId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().is4xxClientError());
  }

  @Test
  @DisplayName("게시물 수정 실패2 테스트 - 수정 내용 미입력한 경우")
  public void updatePost_Fail_2() throws Exception {
    // given
    this.mockUserSetup();

    Long postId = 1L;

    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("");
    requestDto.setContents("게시글 내용 수정");

    PostResponseDto responseDto = new PostResponseDto();
    responseDto.setPostId(postId);
    responseDto.setTitle(requestDto.getTitle());
    responseDto.setContents(requestDto.getContents());
    responseDto.setCreatedAt(LocalDateTime.now());
    responseDto.setModifiedAt(LocalDateTime.now());

    when(postService.updatePost(ArgumentMatchers.eq(postId), ArgumentMatchers.any(PostRequestDto.class),
        ArgumentMatchers.any(User.class)))
        .thenReturn(responseDto);

    // when - then
    mvc.perform(put("/api/posts/{postId}", postId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("게시물 삭제 성공 테스트")
  public void deletePost_Success() throws Exception {
    // given
    this.mockUserSetup();

    Long postId = 1L;

    doNothing().when(postService).deletePost(ArgumentMatchers.eq(postId), ArgumentMatchers.any(User.class));

    // when - then
    mvc.perform(delete("/api/posts/{postId}", postId)
            .contentType(MediaType.APPLICATION_JSON)
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("게시글이 성공적으로 삭제되었습니다."));
  }

  @Test
  @DisplayName("게시물 삭제 실패 테스트 - postId를 입력하지 않은 경우")
  public void deletePost_Fail() throws Exception {
    // given
    this.mockUserSetup();

    Long postId = null;

    doNothing().when(postService).deletePost(ArgumentMatchers.eq(postId), ArgumentMatchers.any(User.class));

    // when - then
    mvc.perform(delete("/api/posts/{postId}", postId)
            .contentType(MediaType.APPLICATION_JSON)
            .principal(mockPrincipal))
            .andDo(print())
            .andExpect(status().is4xxClientError());
  }

}
