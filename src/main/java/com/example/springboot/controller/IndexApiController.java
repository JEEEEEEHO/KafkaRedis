package com.example.springboot.controller;

import com.example.springboot.config.auth.LoginUser;
import com.example.springboot.config.auth.dto.SessionUser;
import com.example.springboot.service.host.HostService;
import com.example.springboot.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexApiController {
    private final PostService postService;
    private final HostService hostService;

    //메인
    @GetMapping("/")
    public String main(Model model, @LoginUser SessionUser user){
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "main";
    }

    //호스트 리스트
    @GetMapping("/hostList")
    public String hostsearch(Model model, @LoginUser SessionUser user){
        if (user != null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("hostList", hostService.findAllDesc());
        }

        return "hostList";
    }

    //마이페이지
    @GetMapping("/mypage")
    public String mypage(Model model, @LoginUser SessionUser user){
        //TODO 하드코딩 부분 - 시큐리티 이후 변경 필요
        model.addAttribute("userName", "김지호");
        return "profile";

            // 1) 회원일 때
//        if (user != null) {
//            model.addAttribute("userName", user.getName());
//            return "profile";
//        } else {
//            // 2) 회원이 아닐때
//            model.addAttribute("msg", "회원만이 이용할 수 있습니다.");
//            model.addAttribute("url", "/");
//            return "alertonlymember";
//        }
    }


    // 공지사항
    @GetMapping("/board")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postService.findAllDesc());
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "board";
    }

}


