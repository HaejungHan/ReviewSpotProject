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

    public PostRequestDto(String title, String contents){
        this.title = title;
        this.contents = contents;
    }

}
