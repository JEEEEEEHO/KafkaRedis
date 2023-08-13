package com.example.springboot.controller;

import com.example.springboot.service.host.HostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HostApiController {
    private final HostServiceImpl hostsService;


    //호스트 리스트


    // 호스트 상세보기

    // 호스트 등록


    // 호스트 수정


    // 호스트 삭제


    // 호스트 검색
    @GetMapping("/host/search")
    public void hostSearch(Model model){

    }

    //호스트 등록화면


}
