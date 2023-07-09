package com.example.springboot.controller;

import com.example.springboot.config.auth.LoginUser;
import com.example.springboot.config.auth.dto.SessionUser;
import com.example.springboot.service.host.HostServiceImpl;
import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HostApiController {
    private final HostServiceImpl hostsService;

    // 호스트 검색
    @GetMapping("/host/search")
    public void hostSearch(Model model){

    }

    //호스트 등록화면
    @GetMapping("/host/save")
    public String hostsSave(Model model, @LoginUser SessionUser user) {
        if (user != null && user.getStatus() != 0) {
            // 1) 호스트 등록 후
            return "hostLists";
        } else {
            // 2) 호스트 등록 전
            model.addAttribute("userid", user.getId());
            return "hostSave";
        }
    }

    @PostMapping("/host")
    public int save(@RequestBody HostSaveRequestDto requestDto){
        return hostsService.save(requestDto);
    }
}
