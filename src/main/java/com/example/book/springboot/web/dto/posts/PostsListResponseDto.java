package com.example.book.springboot.web.dto.posts;

import com.example.book.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsListResponseDto {
    private Long pnum;
    private String title;
    private String author;
    private LocalDateTime modifiedDate;

    @Builder
    public PostsListResponseDto(Posts entity){
        this.pnum = entity.getPnum();
        this.title=entity.getTitle();
        this.author=entity.getAuthor();
        this.modifiedDate=entity.getModifiedDate();
    }
}
