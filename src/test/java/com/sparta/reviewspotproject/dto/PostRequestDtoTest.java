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

public class PostRequestDtoTest {
  private static ValidatorFactory factory;
  private static Validator validator;

  @BeforeAll
  public static void init() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  @DisplayName("게시물의 제목과 내용이 비어있는 경우 validation 테스트")
  public void validation1Test() {
    // given
    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("");
    requestDto.setContents("");

    // when
    Set<ConstraintViolation<PostRequestDto>> violations = validator.validate(requestDto);

    // then
    // violations에 현재 유효성 검증된 내용이 들어가 있음 test 성공해야 유효성 검사 성공
    assertFalse(violations.isEmpty());
    assertEquals(2, violations.size());
  }

  @Test
  @DisplayName("게시물의 제목과 내용이 있는 경우 validation 테스트")
  public void validation2Test() {
    // given
    PostRequestDto requestDto = new PostRequestDto();
    requestDto.setTitle("게시물의 제목입니다.");
    requestDto.setContents("게시물의 내용입니다.");

    // when
    Set<ConstraintViolation<PostRequestDto>> violations = validator.validate(requestDto);

    // then
    // violations에 유효성 검증된 내용이 없음 test 성공해야 유효성 검사 성공
    assertTrue(violations.isEmpty());
    assertEquals(0, violations.size());
  }

}
