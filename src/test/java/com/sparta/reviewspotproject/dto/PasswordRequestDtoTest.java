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

public class PasswordRequestDtoTest {

  private static ValidatorFactory factory;
  private static Validator validator;

  @BeforeAll
  public static void init() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  @DisplayName("비밀번호를 입력하지 않은 경우 validation 테스트")
  public void validation1Test() {
    // given
    PasswordRequestDto requestDto = new PasswordRequestDto();
    requestDto.setPassword("");
    requestDto.setChangePassword("");

    // when
    Set<ConstraintViolation<PasswordRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(2, violations.size());

  }

  @Test
  @DisplayName("현재 비밀번호가 정규식 표현에 맞지 않은경우 validation 테스트")
  public void validation2Test() {
    // given
    PasswordRequestDto requestDto = new PasswordRequestDto();
    requestDto.setPassword("@hh1234");
    requestDto.setChangePassword("@hhanni12345");

    // when
    Set<ConstraintViolation<PasswordRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());

  }

  @Test
  @DisplayName("변경할 비밀번호가 정규식 표현에 맞지 않은경우 validation 테스트")
  public void validation3Test() {
    // given
    PasswordRequestDto requestDto = new PasswordRequestDto();
    requestDto.setPassword("@hhanni12345");
    requestDto.setChangePassword("hhanni1");

    // when
    Set<ConstraintViolation<PasswordRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());

  }

  @Test
  @DisplayName("비밀번호가 정규식 표현에 맞게 입력된 경우 validation 테스트")
  public void validation4Test() {
    // given
    PasswordRequestDto requestDto = new PasswordRequestDto();
    requestDto.setPassword("@hhanni12345");
    requestDto.setChangePassword("@hhanni456789");

    // when
    Set<ConstraintViolation<PasswordRequestDto>> violations = validator.validate(requestDto);

    // then
    assertTrue(violations.isEmpty());
    assertEquals(0, violations.size());

  }
}
