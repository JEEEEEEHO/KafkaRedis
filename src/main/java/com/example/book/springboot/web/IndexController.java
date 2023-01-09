package com.example.book.springboot.web;

import com.example.book.springboot.config.auth.LoginUser;
import com.example.book.springboot.config.auth.dto.SessionUser;
import com.example.book.springboot.service.posts.PostsService;
import com.example.book.springboot.web.dto.posts.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final PostsService postsService;

    //메인
    @GetMapping("/")
    public String main(Model model, @LoginUser SessionUser user){
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "main";
    }
    //호스트 찾기
    @GetMapping("/hostsearch")
    public String hostsearch(Model model, @LoginUser SessionUser user){
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "hostsearch";
    }

    //마이페이지
    @GetMapping("/mypage")
    public String mypage(Model model, @LoginUser SessionUser user){
            // 1) 회원일 때
        if (user != null) {
            model.addAttribute("userName", user.getName());
            return "profile";
        } else {
            // 2) 회원이 아닐때
            model.addAttribute("msg", "회원만이 이용할 수 있습니다.");
            model.addAttribute("url", "/");
            return "alertonlymember";
        }
    }

    //호스트 등록화면
    @GetMapping("/hosts/save")
    public String hostsSave(Model model, @LoginUser SessionUser user) {
        if (user != null && user.getStatus() != 0) {
            // 1) 호스트 등록 후
            return "hosts-info";
        } else {
            // 2) 호스트 등록 전
            return "hosts-save";
        }
    }

    // 공지사항
    @GetMapping("/board")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "board-view";
    }

    // 공지사항 등록 페이지 이동
    @GetMapping("/posts/save")
    public String postsSave(Model model, @LoginUser SessionUser user) {
            // 1) 회원일 때
        if (user != null) {
            model.addAttribute("userName", user.getName());
            return "posts-save";
        }else {
            // 2) 회원이 아닐때
            model.addAttribute("msg", "회원만이 이용할 수 있습니다.");
            model.addAttribute("url", "/");
            return "alertonlymember";
        }
    }

    //공지사항 상세 페이지 이동
    @GetMapping("/posts/detail/{pnum}")
    public String postsUpdate(@PathVariable Long pnum, Model model, @LoginUser SessionUser user) {
        PostsResponseDto dto = postsService.findById(pnum);
        model.addAttribute("post", dto);
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "posts-update";
    }
}


