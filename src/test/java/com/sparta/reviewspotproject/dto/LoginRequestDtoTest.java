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

public class LoginRequestDtoTest {

  private static ValidatorFactory factory;
  private static Validator validator;

  @BeforeAll
  public static void init() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  @DisplayName("아이디와 비밀번호가 비어있는 경우 validation 테스트")
  public void validation1Test() {
    // given
    LoginRequestDto requestDto = new LoginRequestDto("", "");

    // when
    Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(2, violations.size());

  }

  @Test
  @DisplayName("아이디가 정규식표현과 맞지 않을 경우 validation 테스트")
  public void validation2Test() {
    // given
    LoginRequestDto requestDto = new LoginRequestDto("hani1234", "@hhanni123456");

    // when
    Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());

  }

  @Test
  @DisplayName("비밀번호가 정규식표현과 맞지 않을 경우 validation 테스트")
  public void validation3Test() {
    // given
    LoginRequestDto requestDto = new LoginRequestDto("hani12345678", "@hhanni12");

    // when
    Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());

  }

  @Test
  @DisplayName("아이디와 비밀번호가 정규식표현과 맞지 않을 경우 validation 테스트")
  public void validation4Test() {
    // given
    LoginRequestDto requestDto = new LoginRequestDto("hani123", "@hhanni12");

    // when
    Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(2, violations.size());

  }

  @Test
  @DisplayName("아이디와 비밀번호가 정규식표현에 맞게 입력한 경우 validation 테스트")
  public void validation5Test() {
    // given
    LoginRequestDto requestDto = new LoginRequestDto("hani12345678", "@hhanni123456");

    // when
    Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(requestDto);

    // then
    assertTrue(violations.isEmpty());
    assertEquals(0, violations.size());

  }


}
