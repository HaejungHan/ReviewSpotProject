package com.sparta.reviewspotproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {
    @Pattern(
        regexp = "^[a-zA-Z0-9]{10,20}$",
        message = "사용자 이름은 대소문자 포함 영문자와 숫자로 이루어진 10자에서 20자 사이여야 합니다."
    )
    private String userId;

    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{10,}$",
        message = "비밀번호는 대소문자 영문, 숫자, 특수문자를 최소 1글자씩 포함하며 최소 10자 이상이어야 합니다."
    )
    private String password;

}
