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

public class EmailRequestDtoTest {
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
    EmailRequestDto requestDto = new EmailRequestDto("emailvalidation");

    // when
    Set<ConstraintViolation<EmailRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
  }

  @Test
  @DisplayName("이메일 주소를 입력하지 않은 경우 validation 테스트")
  public void validation2Test() {
    // given
    EmailRequestDto requestDto = new EmailRequestDto("");

    // when
    Set<ConstraintViolation<EmailRequestDto>> violations = validator.validate(requestDto);

    // then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
  }

  @Test
  @DisplayName("유효한 이메일을 입력한 경우 validation 테스트")
  public void validation3Test() {
    // given
    EmailRequestDto requestDto = new EmailRequestDto("validation@gmail.com");

    // when
    Set<ConstraintViolation<EmailRequestDto>> violations = validator.validate(requestDto);

    // then
    assertTrue(violations.isEmpty());
    assertEquals(0, violations.size());
  }

}
