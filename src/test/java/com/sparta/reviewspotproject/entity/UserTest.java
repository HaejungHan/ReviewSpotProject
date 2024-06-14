package com.sparta.reviewspotproject.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sparta.reviewspotproject.dto.ProfileRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

  @Test
  @DisplayName("사용자 entity 테스트")
  public void entityTest() {
    // given
    String userId = "haniyoni1234";
    String password = "@haniandyoni1234";
    String userName = "하니와요니";
    String email = "hhanni0705@gmail.com";
    UserStatus userStatus = UserStatus.MEMBER;

    // when
    User user = new User(userId, password, userName, email, userStatus);

    // then
    assertEquals(userId, user.getUserId());
    assertEquals(password, user.getPassword());
    assertEquals(userName, user.getUserName());
    assertEquals(email, user.getEmail());
    assertEquals(userStatus, user.getUserStatus());
    assertNotNull(user.getCreatedAt());
  }

  @Test
  @DisplayName("사용자의 프로필 update 메서드 테스트")
  public void updateTest() {
    // given
    ProfileRequestDto requestDto = new ProfileRequestDto("요니와하니", "만나서 반갑습니다. 여러분");

    // when
    User user = new User("haniyoni1234", "@haniandyoni1234", "하니와요니", "hhanni0705@gmail.com",
        UserStatus.MEMBER);
    user.update(requestDto);

    // then
    assertEquals("요니와하니", requestDto.getUserName());
    assertEquals("만나서 반갑습니다. 여러분", requestDto.getTagLine());
    assertNotNull(user.getModifiedAt());

  }

}
