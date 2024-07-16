package com.example.springboot.controller;

import com.example.springboot.controller.dto.host.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class HostApiController {
    private final HostService hostsService;
    private final UserRepository userRepository;
    private final HostMainImgRepository hostMainImgRepository;
    private final HostImgRepository hostImgRepository;


    /**
     * Response 호스트 전체 리스트
     * @return HostListResponseDto
     * */
    @GetMapping("/api/host/list")
    public List<HostListResponseDto> viewHostList(){
        return hostsService.findAllHost();
    }

    /**
     *                                                                                                                      `                               `                           `                                   `                           `                                                                                                                                                                                                   `                                                                                                                                                                                                                                                                    * Response 호스트 검색 searchHost +
     * @param
     * @return
     * */
    @GetMapping("/api/host/search")
    public List<HostListResponseDto> searchHostList(HostsearchReqeustDto hostsearchReqeustDto) throws ParseException {
        return hostsService.searchHost(hostsearchReqeustDto);
    }

    /**
     * Response 호스트 상세보기
     * @param
     * @return
     * */
    @GetMapping("/api/host/{hnum}")
    public HostDetailResponseDto hostDetail(@PathVariable String hnum, Model model){
        return hostsService.viewHostDetail(hnum);
    }

    /**
     * Response 호스트 내용 보기
     * @param principal
     * @return HostSaveResponseDto
     * */
    @GetMapping("/api/host/info")
    public HostSaveResponseDto savedHostInfo(Principal principal) throws Exception {
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

    /**
     * Request 호스트 등록(정보+메인이미지)
     * @param principal
     * @param file
     * @param saveRequestDto
     * @return String
     * */
    @PostMapping(value = "/api/host/save", consumes = "multipart/form-data")
    public String saveHost(Principal principal, @RequestPart(value = "file") MultipartFile file, @RequestPart(value = "hostData") HostSaveRequestDto saveRequestDto) throws IOException {
        // token 값에 저장되어 있는 userId
        String userId = principal.getName();
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            // user 정보 Dto 에 담기
            saveRequestDto.setUser(user.get());
        }
        return hostsService.save(saveRequestDto, file);
    }

    /**
     * Request 호스트 등록(이미지)
     * @param files
     * @param hostNum
     * @return void
     * */
    @PostMapping(value = "/api/host/saveImg", consumes = "multipart/form-data")
    public void saveHostImgs(@RequestPart("files") MultipartFile[] files, @RequestPart(value = "hnum") String hostNum ) throws IOException {
        hostsService.saveImgs(files, hostNum);
    }

    /**
     * 호스트 이미지 가져오기
     * @param fileName
     * @return ResponseEntity
     * */
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
    /**
     * Request 호스트 수정 (정보+메인이미지)
     * @param file
     * @param updateRequestDto
     * @return String
     * */
    @PutMapping("/api/host/update")
    public String updateHost(@RequestPart(value = "file") MultipartFile file, @RequestPart(value = "hostData") HostUpdateRequestDto updateRequestDto) throws IOException {
        return hostsService.update(updateRequestDto, file);
    }

    /**
     * Request 호스트 이미지 수정
     * @param files
     * @param hostNum
     * @param updateRequestDto
     * @return void
     * */
    @PutMapping(value = "/api/host/updateImg" , consumes = "multipart/form-data")
    public void updateHostImgs(@RequestPart("files") MultipartFile[] files
            , @RequestPart(value = "hnum") String hostNum
            , @ModelAttribute(value = "deleteFiles") HostUpdateRequestDto updateRequestDto) throws IOException {
        hostsService.updateImgs(files,hostNum,updateRequestDto);
    }

    /**
     * Request 호스트 삭제
     * @param
     * @param
     * @param
     * @return
     * */


}
