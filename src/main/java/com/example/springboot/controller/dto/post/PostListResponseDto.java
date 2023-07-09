package com.example.springboot.controller.dto.post;

import com.example.springboot.domain.post.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListResponseDto {
    private Long pnum;
    private String title;
    private String author;
    private LocalDateTime modifiedDate;

    @Builder
    public PostListResponseDto(Post entity){
        this.pnum = entity.getPnum();
        this.title=entity.getTitle();
        this.author=entity.getAuthor();
        this.modifiedDate=entity.getModifiedDate();
    }
}
