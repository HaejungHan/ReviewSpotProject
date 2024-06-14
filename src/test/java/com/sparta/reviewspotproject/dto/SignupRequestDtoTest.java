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

public class SignupRequestDtoTest {

  private static ValidatorFactory factory;
  private static Validator validator;

  @BeforeAll
  public static void init() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  @DisplayName("아이디와 비밀번호를 입력하지 않은 경우 validation 테스트")
  public void validation1Test() {
    // given
    SignupRequestDto requestDto = new SignupRequestDto();
    requestDto.setUserId("");
    requestDto.setPassword("");
    requestDto.setUserName("하니와요니");
    requestDto.setEmail("validaion@email.com");

    // when
    Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(2, violations.size());
  }

  @Test
  @DisplayName("유효하지 않은 이메일을 입력한 경우 validation 테스트")
  public void validation2Test() {
    // given
    SignupRequestDto requestDto = new SignupRequestDto();
    requestDto.setUserId("hhanni123456");
    requestDto.setPassword("@hhanni123456");
    requestDto.setUserName("요니와하니");
    requestDto.setEmail("validaion");

    // when
    Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
  }

  @Test
  @DisplayName("닉네임을 입력하지 않은 경우 validation 테스트")
  public void validation3Test() {
    // given
    SignupRequestDto requestDto = new SignupRequestDto();
    requestDto.setUserId("hhanni123456");
    requestDto.setPassword("@hhanni123456");
    requestDto.setUserName("");
    requestDto.setEmail("validaion@gmail.com");

    // when
    Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
  }

  @Test
  @DisplayName("아이디,비밀번호,닉네임,이메일주소 정확하게 입력한 경우 validation 테스트")
  public void validation4Test() {
    // given
    SignupRequestDto requestDto = new SignupRequestDto();
    requestDto.setUserId("hhanni123456");
    requestDto.setPassword("@hhanni123456");
    requestDto.setUserName("요니와 하니");
    requestDto.setEmail("validaion@gmail.com");

    // when
    Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(requestDto);

    // then
    assertTrue(violations.isEmpty());
    assertEquals(0, violations.size());
  }
}
