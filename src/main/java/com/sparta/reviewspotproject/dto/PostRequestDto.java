package com.sparta.reviewspotproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String contents;


}
