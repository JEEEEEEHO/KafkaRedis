package com.example.springboot.controller;

import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.service.host.HostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class HostApiController {
    private final HostServiceImpl hostsService;


    //호스트 리스트 Response


    // 호스트 상세보기 Response

    // 호스트 이미지 등록
    @PostMapping("/api/host/saveImgs")
    public void saveImgs(@RequestPart("files") MultipartFile[] files){
        hostsService.saveImgs(files);
    }

    // 호스트 등록 Request
    @PostMapping("/api/host/save")
    public void save(HostSaveRequestDto saveRequestDto){
        hostsService.save(saveRequestDto);
    }

    // 호스트 수정 Request


    // 호스트 삭제


    // 호스트 검색 Response
    @GetMapping("/host/search")
    public void hostSearch(Model model){

    }

}
