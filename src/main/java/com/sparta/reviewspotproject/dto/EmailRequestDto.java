package com.sparta.reviewspotproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequestDto {
    @Email(message = "유효한 이메일을 입력해주세요.")
    @NotBlank(message = "Email을 입력해주세요.")
    private String email;

}
