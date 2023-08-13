package com.example.springboot.controller;

import com.example.springboot.service.host.HostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final HostService hostService;

    //메인
    @GetMapping("/")
    public String main(HttpServletRequest request, Model model){

        return "main";
    }

    //마이페이지
    @GetMapping("/mypage")
    public String mypage(Model model){

            // 2) 회원이 아닐때
            model.addAttribute("msg", "회원만이 이용할 수 있습니다.");
            model.addAttribute("url", "/");
            return "/layout/alertonlymember";
        }


}


