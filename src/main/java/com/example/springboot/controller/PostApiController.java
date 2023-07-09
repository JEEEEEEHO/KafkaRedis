package com.example.springboot.controller;

import com.example.springboot.config.auth.LoginUser;
import com.example.springboot.config.auth.dto.SessionUser;
import com.example.springboot.controller.dto.post.PostResponseDto;
import com.example.springboot.service.post.PostService;
import com.example.springboot.controller.dto.post.PostSaveRequestDto;
import com.example.springboot.controller.dto.post.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostService postService;


    // 공지사항 등록 페이지 이동
    @GetMapping("/post/save")
    public String postSave(Model model, @LoginUser SessionUser user) {
        // 1) 회원일 때
        if (user != null) {
            model.addAttribute("userName", user.getName());
            return "postSave";
        }else {
            // 2) 회원이 아닐때
            model.addAttribute("msg", "회원만이 이용할 수 있습니다.");
            model.addAttribute("url", "/board");
            return "alertonlymember";
        }
    }

    //공지사항 상세 페이지 이동
    @GetMapping("/post/{pnum}")
    public String postUpdate(@PathVariable Long pnum, Model model, @LoginUser SessionUser user) {
        PostResponseDto dto = postService.findById(pnum);
        model.addAttribute("post", dto);
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "postUpdate";
    }

    // 저장
    @PostMapping("/post/save")
    public Long save(@RequestBody PostSaveRequestDto requestDto) {
        return postService.save(requestDto);
    }

    // 수정
    @PutMapping("/post/{pnum}")
    public Long update(@PathVariable Long pnum, @RequestBody PostUpdateRequestDto requestDto) {
        return postService.update(pnum, requestDto);
    }

    // 상세보기
    @GetMapping("/post/{id}")
    public PostResponseDto findById(@PathVariable Long id) {
        return postService.findById(id);
    }


    // 삭제
    @DeleteMapping("/post/{pnum}")
    public Long delete(@PathVariable Long pnum){
        postService.delete(pnum);
        return pnum;
    }
}