package com.example.book.springboot.web.dto.posts;
// 업데이트 요청용 DTO

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    private String title;
    private String content;

    @Builder
    public PostsUpdateRequestDto(String title, String content){
        this.title=title;
        this.content=content;
    }
}
