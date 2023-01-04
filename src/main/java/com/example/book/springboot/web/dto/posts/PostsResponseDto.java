package com.example.book.springboot.web.dto.posts;

import com.example.book.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;

// 조회 응답용 DTO
@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    @Builder
    public PostsResponseDto(Posts entity){
        this.id = entity.getPnum();
        this.title=entity.getTitle();
        this.content=entity.getContent();
        this.author=entity.getAuthor();
    }
}
