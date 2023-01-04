package com.example.book.springboot.web;

import com.example.book.springboot.service.posts.PostsService;
import com.example.book.springboot.web.dto.posts.PostsResponseDto;
import com.example.book.springboot.web.dto.posts.PostsSaveRequestDto;
import com.example.book.springboot.web.dto.posts.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{pnum}")
    public Long update(@PathVariable Long pnum, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(pnum, requestDto);
    }

/*    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }*/

    @DeleteMapping("/api/v1/posts/{pnum}")
    public Long delete(@PathVariable Long pnum){
        postsService.delete(pnum);
        return pnum;
    }
}