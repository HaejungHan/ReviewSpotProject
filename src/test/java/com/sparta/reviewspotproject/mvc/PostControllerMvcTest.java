package com.sparta.reviewspotproject.mvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    String userName = "sollertia4351";
    String email = "hhanni0705@gmail.com";
    UserStatus userStatus = UserStatus.MEMBER;
    User testUser = new User(userId, userName, password, email, userStatus);
    UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
    mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "",
        testUserDetails.getAuthorities());
  }

  @Test
  @DisplayName("게시글 작성")
  public void createPost() throws Exception {
    // given
    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("게시글 제목");
    requestDto.setContents("게시글 내용");

    PostResponseDto responseDto = new PostResponseDto();
    responseDto.setPostId(1L);
    responseDto.setTitle("게시글 제목");
    responseDto.setContents("게시글 내용");

    when(postService.createPost(ArgumentMatchers.any(PostRequestDto.class),
        ArgumentMatchers.any(User.class)))
        .thenReturn(responseDto);

    // when - then
    mvc.perform(post("/api/posts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(requestDto))
        .principal(mockPrincipal))
        .andExpect(status().isOk());
  }
}
