package com.sparta.reviewspotproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostRequestDto {
    @NotBlank(message = "게시물의 제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "게시물의 내용을 입력해주세요.")
    private String contents;


}
