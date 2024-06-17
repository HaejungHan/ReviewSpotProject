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

public class CommentRequestDtoTest {
  private static ValidatorFactory factory;
  private static Validator validator;

  @BeforeAll
  public static void init() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  @DisplayName("댓글의 내용이 비어있는 경우 validation 테스트")
  public void validation1Test() {
    // given
    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("");

    // when
    Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());

  }

  @Test
  @DisplayName("댓글의 내용이 있는 경우 validation 테스트")
  public void validation2Test() {
    // given
    CommentRequestDto requestDto = new CommentRequestDto();
    requestDto.setContents("댓글의 내용입니다.");

    // when
    Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(requestDto);

    // then
    assertTrue(violations.isEmpty());
    assertEquals(0, violations.size());

  }


}
