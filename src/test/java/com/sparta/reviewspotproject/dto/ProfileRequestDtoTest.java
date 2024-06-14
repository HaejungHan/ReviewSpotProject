package com.sparta.reviewspotproject.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProfileRequestDtoTest {
  private static ValidatorFactory factory;
  private static Validator validator;

  @BeforeAll
  public static void init() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  @DisplayName("사용자의 닉네임과 한줄 소개를 입력하지 않은 경우 validation 테스트")
  public void validation1Test() {
    // given
    ProfileRequestDto requestDto = new ProfileRequestDto();
    requestDto.setUserName("");
    requestDto.setTagLine("");

    // when
    Set<ConstraintViolation<ProfileRequestDto>> violations = validator.validate(requestDto);

    // then
    // violations에 현재 유효성 검증된 내용이 들어가 있음 test 성공해야 유효성 검사 성공
    assertFalse(violations.isEmpty());
    assertEquals(2, violations.size());
  }

  @Test
  @DisplayName("게시물의 제목과 내용이 있는 경우 validation 테스트")
  public void validationSuccessTest() {
    // given
    ProfileRequestDto requestDto = new ProfileRequestDto();
    requestDto.setUserName("게시물의 제목입니다.");
    requestDto.setTagLine("게시물의 내용입니다.");
    // when
    Set<ConstraintViolation<ProfileRequestDto>> violations = validator.validate(requestDto);

    // then
    // violations에 유효성 검증된 내용이 없음 test 성공해야 유효성 검사 성공
    assertTrue(violations.isEmpty());
    assertEquals(0, violations.size());
  }


}
