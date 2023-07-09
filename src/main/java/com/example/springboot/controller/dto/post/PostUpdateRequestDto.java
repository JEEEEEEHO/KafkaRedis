package com.example.springboot.controller.dto.post;
// 업데이트 요청용 DTO

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {
    private String title;
    private String content;

    @Builder
    public PostUpdateRequestDto(String title, String content){
        this.title=title;
        this.content=content;
    }
}
