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
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class HostApiController {
    private final UserRepository userRepository;
    private final HostService hostsService;
    private final HostMainImgRepository hostMainImgRepository;


    // 호스트 검색 Response
    @GetMapping("/host/list")
    public void hostSearch(Model model){

    }


    // 호스트 검색 상세보기 Response


    // 호스트 등록 내용 보기 Response
    // User에 따라 Host를 찾고, 그 num 에 해당하는 이미지 파일들을 포함
    @GetMapping("/api/host/info")
    public HostSaveResponseDto hostSaveResponseDto(Principal principal) throws Exception {
        // token 값에 저장되어 있는 userId
        String userId = principal.getName();
        Optional<User> user = userRepository.findById(userId);

       if(user.isPresent()){
           // user 를 통해 등록되어 있는 host 정보 가져오기
           return hostsService.findHostInfo(user.get());
       } else{
           throw new Exception("No User Info : forbidden access");
       }
    }
    // 호스트 등록 내용 보기 (메인이미지 가져오기)
    @GetMapping("image")
    public ResponseEntity<?> returnImage(@RequestParam("image") String imgPid) {
        // imgPid의 자료형은? Long으로 보내서, Long으로 받아오면 그대로, 아니면 변형
        Optional<HostMainImg> hostMainImg = hostMainImgRepository.findById(Long.parseLong(imgPid)); //이미지가 저장된 위치
        if(hostMainImg.isPresent()){
            String path = hostMainImg.get().getFileImgPath();
            Resource resource = new FileSystemResource(path);
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    // 호스트 등록 내용 보기 (기본이미지 가져오기)


    // 호스트 등록(정보+메인이미지) Request
    @PostMapping(value = "/api/host/save", consumes = "multipart/form-data")
    public String save(Principal principal, @RequestPart(value = "file") MultipartFile file, @RequestPart(value = "hostData") HostSaveRequestDto saveRequestDto) throws IOException {
        // token 값에 저장되어 있는 userId
        String userId = principal.getName();
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            // user 정보 Dto 에 담기
            saveRequestDto.setUser(user.get());
        }
        return hostsService.save(saveRequestDto, file);
    }

    // 호스트 이미지 등록 Request
    @PostMapping(value = "/api/host/saveImg", consumes = "multipart/form-data")
    public void saveImgs(@RequestPart("files") MultipartFile[] files, @RequestPart(value = "hnum") String hostNum ){
        hostsService.saveImgs(files, hostNum);
    }

    // 호스트 수정 Request
    //@PutMapping("api/host/update")


    // 호스트 삭제


}
