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

public class VerifyCodeRequestDtoTest {
  private static ValidatorFactory factory;
  private static Validator validator;

  @BeforeAll
  public static void init() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  @DisplayName("유효한 이메일이 아닌 경우 validation 테스트")
  public void validation1Test() {
    // given
    VerifyCodeRequestDto requestDto = new VerifyCodeRequestDto();
    requestDto.setEmail("email");
    requestDto.setVerificationCode("123456");

    // when
    Set<ConstraintViolation<VerifyCodeRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
  }

  @Test
  @DisplayName("이메일 주소를 입력하지 않은 경우 validation 테스트")
  public void validation2Test() {
    // given
    VerifyCodeRequestDto requestDto = new VerifyCodeRequestDto();
    requestDto.setEmail("");
    requestDto.setVerificationCode("123456");

    // when
    Set<ConstraintViolation<VerifyCodeRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
  }

  @Test
  @DisplayName("인증코드를 입력하지 않은 경우 validation 테스트")
  public void validation3Test() {
    // given
    VerifyCodeRequestDto requestDto = new VerifyCodeRequestDto();
    requestDto.setEmail("validaion@gmail.com");
    requestDto.setVerificationCode("");

    // when
    Set<ConstraintViolation<VerifyCodeRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
  }

  @Test
  @DisplayName("유효한 이메일, 인증코드를 정확히 입력한 경우 validation 테스트")
  public void validation4Test() {
    // given
    VerifyCodeRequestDto requestDto = new VerifyCodeRequestDto();
    requestDto.setEmail("validation@gmail.com");
    requestDto.setVerificationCode("123456");

    // when
    Set<ConstraintViolation<VerifyCodeRequestDto>> violations = validator.validate(requestDto);

    // then
    assertTrue(violations.isEmpty());
    assertEquals(0, violations.size());
  }

}
