package com.example.springboot.controller;

import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.controller.dto.host.HostSaveResponseDto;
import com.example.springboot.domain.host.*;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import com.example.springboot.service.host.HostService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class HostApiController {
    private final UserRepository userRepository;
    private final HostService hostsService;
    private final HostMainImgRepository hostMainImgRepository;
    private final HostImgRepository hostImgRepository;


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
    @CrossOrigin
    @GetMapping(value = "/image/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> returnImage(@PathVariable String fileName) throws Exception {
        String path = "";
        HostMainImg hostMainImg = hostMainImgRepository.findMainImg(fileName);
        if(hostMainImg!=null){
            path = hostMainImg.getFilepath(); //이미지가 저장된 위치
            Resource resource = new FileSystemResource(path);
            HttpHeaders headers = new HttpHeaders();
            Path filepath = null;
            filepath = Paths.get(path);
            headers.add("Content-Type", Files.probeContentType(filepath));
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } else{
            HostImg hostImg = hostImgRepository.findImg(fileName);
            if(hostImg!=null){
                path = hostImg.getFilepath();
                Resource resource = new FileSystemResource(path);
                HttpHeaders headers = new HttpHeaders();
                Path filepath = null;
                filepath = Paths.get(path);
                headers.add("Content-Type", Files.probeContentType(filepath));
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else{
                throw new Exception("No img");
            }
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
    public void saveImgs(@RequestPart("files") MultipartFile[] files, @RequestPart(value = "hnum") String hostNum ) throws IOException {
        hostsService.saveImgs(files, hostNum);
    }

    // 호스트 수정 Request (hostnum 을 찾아서 엎어치기 )
    @PutMapping("api/host/update")
    public String update(@RequestPart(value = "file") MultipartFile file, @RequestPart(value = "hostData") HostSaveRequestDto saveRequestDto){
        return hostsService.update(saveRequestDto, file);
    }

    // 호스트 이미지 수정 ( 이 경우엔 이미지 업로드의 개수 한정이 있기 때문에 삭제 - > 입력)


    // 호스트 삭제


}
