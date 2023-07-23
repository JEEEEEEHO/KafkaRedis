package com.example.springboot.controller;

import com.example.springboot.controller.dto.post.PostListResponseDto;
import com.example.springboot.controller.dto.post.PostResponseDto;
import com.example.springboot.domain.post.Post;
import com.example.springboot.service.post.PostService;
import com.example.springboot.controller.dto.post.PostSaveRequestDto;
import com.example.springboot.controller.dto.post.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;
    // 리스트
    @GetMapping("/api/post/list")
    public List<PostListResponseDto> postList(){
        return postService.findAllDesc();
    }

    // 저장
    @PostMapping("/api/post/save")
    public Long save(@RequestBody PostSaveRequestDto requestDto) {
        return postService.save(requestDto);
    }


    // 수정
    @PutMapping("/api/post/{pnum}")
    public Long update(@PathVariable Long pnum, @RequestBody PostUpdateRequestDto requestDto) {
        return postService.update(pnum, requestDto);
    }

    // 삭제
    @DeleteMapping("/api/post/{pnum}")
    public Long delete(@PathVariable Long pnum) {
        postService.delete(pnum);
        return pnum;
    }
}