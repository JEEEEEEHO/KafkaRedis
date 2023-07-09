package com.example.springboot.controller.dto.post;

import com.example.springboot.domain.post.Post;
import lombok.Builder;
import lombok.Getter;

// 조회 응답용 DTO
@Getter
public class PostResponseDto {
    private Long pnum;
    private String title;
    private String content;
    private String author;

    @Builder
    public PostResponseDto(Post entity){
        this.pnum = entity.getPnum();
        this.title=entity.getTitle();
        this.content=entity.getContent();
        this.author=entity.getAuthor();
    }
}
