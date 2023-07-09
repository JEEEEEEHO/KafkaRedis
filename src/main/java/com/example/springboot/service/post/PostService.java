package com.example.springboot.service.post;


import com.example.springboot.controller.dto.post.PostListResponseDto;
import com.example.springboot.controller.dto.post.PostResponseDto;
import com.example.springboot.controller.dto.post.PostSaveRequestDto;
import com.example.springboot.controller.dto.post.PostUpdateRequestDto;

import java.util.List;

public interface PostService {
    // 저장
    long save(PostSaveRequestDto dto);

    //수정
    Long update (Long num, PostUpdateRequestDto dto);

    //조회
    List<PostListResponseDto> findAllDesc();

    //상세
    PostResponseDto findById(Long num);

    //삭제
    void delete(Long num);
}
