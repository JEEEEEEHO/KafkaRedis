package com.example.springboot.controller;

import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.domain.host.HostImg;
import com.example.springboot.domain.host.HostImgRepository;
import com.example.springboot.domain.host.HostMainImgRepository;
import com.example.springboot.service.host.HostServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class HostApiController {
    private final HostServiceImpl hostsService;
    private final HostMainImgRepository hostMainImgRepository;
    private final HostImgRepository hostImgRepository;

    //호스트 리스트 Response


    // 호스트 상세보기 Response


    // 호스트 등록 Request
    @PostMapping("/api/host/save")
    public void save(@RequestPart(value = "file") MultipartFile file, @RequestPart(value = "hostData") HostSaveRequestDto saveRequestDto) throws IOException {
        hostsService.save(saveRequestDto, file);
    }

    // 호스트 이미지 등록
    @PostMapping("/api/host/saveImgs")
    public void saveImgs(@RequestPart("files") MultipartFile[] files, String hostNum ){
        hostsService.saveImgs(files);
    }

    // 호스트 수정 Request


    // 호스트 삭제


    // 호스트 검색 Response
    @GetMapping("/host/search")
    public void hostSearch(Model model){

    }

}
