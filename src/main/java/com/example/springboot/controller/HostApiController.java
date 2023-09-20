package com.example.springboot.controller;

import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.controller.dto.host.HostSaveResponseDto;
import com.example.springboot.domain.host.*;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import com.example.springboot.security.JwtAuthenticationFilter;
import com.example.springboot.security.TokenProvider;
import com.example.springboot.service.host.HostService;
import com.example.springboot.service.host.HostServiceImpl;
import com.example.springboot.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class HostApiController {
    private final UserRepository userRepository;
    private final HostRepository hostRepository;
    private final HostService hostsService;
    private final HostMainImgRepository hostMainImgRepository;
    private final HostImgRepository hostImgRepository;


    // 호스트 검색 Response
    @GetMapping("/host/search")
    public void hostSearch(Model model){

    }


    // 호스트 검색 상세보기 Response



    // 호스트 등록 Request
    @PostMapping(value = "/api/host/save", consumes = "multipart/form-data")
    public String save(@RequestPart(value = "file") MultipartFile file, @RequestPart(value = "hostData") HostSaveRequestDto saveRequestDto) throws IOException {
        return hostsService.save(saveRequestDto, file);
    }

    // 호스트 이미지 등록 Request
    @PostMapping(value = "/api/host/saveImg", consumes = "multipart/form-data")
    public void saveImgs(@RequestPart("files") MultipartFile[] files, @RequestPart(value = "hnum") String hostNum ){
        hostsService.saveImgs(files, hostNum);
    }

    // 호스트 등록 내용 보기 Response User에 따라 Host를 찾고, 그 num 에 해당하는 이미지 파일들
    @GetMapping("/api/host/saveInfo")
    public HostSaveResponseDto hostSaveResponseDto(HttpServletRequest request){
        HttpSession session = request.getSession();
        String userEmail = (String) session.getAttribute("email");

        User user = userRepository.findByEmail(userEmail);

        return hostsService.findHostInfo(user);
    }

    // 호스트 수정 Request
    //@PutMapping("api/host/update")


    // 호스트 삭제


}
