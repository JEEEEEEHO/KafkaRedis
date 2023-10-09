package com.example.springboot.service.host;

import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.controller.dto.host.HostSaveResponseDto;
import com.example.springboot.domain.host.*;
import com.example.springboot.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HostServiceImpl implements HostService {

    private final HostRepository hostRepository;
    private final HostMainImgRepository hostMainImgRepository;
    private final HostImgRepository hostImgRepository;
    String filepath = "C:\\Users/rlawl/IdeaProjects/WWOOF-side-project/src/main/resources/static/img/Host/";
    // Host 정보 불러오기
    @Override
    public HostSaveResponseDto findHostInfo(User user,  HostSaveResponseDto hostSaveResponseDto) {

        // userid로 host 정보를 찾음
        long count = hostRepository.findByUidCount(user);
        // Host 정보가 있다면
        if(count>0){
            Host host = hostRepository.findByUid(user);
            // Host 메인 이미지
            HostMainImg hostMainImg = hostMainImgRepository.findMainImg(host.getHnum());
            // Host 이미지 들
            List<HostImg> hostImgList = hostImgRepository.findAllImgs(host.getHnum());
            // DTO에 담는 부분
            hostSaveResponseDto.setUser(user);
            hostSaveResponseDto.setHostMainImg(hostMainImg);
            hostSaveResponseDto.setHostImg(hostImgList);
            hostSaveResponseDto.setRegion(host.getRegion());
            hostSaveResponseDto.setGender(host.getGender());
            hostSaveResponseDto.setAge(host.getAge());
            hostSaveResponseDto.setFarmsts(host.getFarmsts());
            hostSaveResponseDto.setShortintro(host.getShortintro());
            hostSaveResponseDto.setIntro(host.getIntro());
            hostSaveResponseDto.setLat(host.getLat());
            hostSaveResponseDto.setLng(host.getLng());
            hostSaveResponseDto.setMaxPpl(host.getMaxPpl());
            hostSaveResponseDto.setApprvYn(host.getApprvYn());
//            hostSaveResponseDto.builder()
//                    .user(user)
//                    .hostMainImg(hostMainImg)
//                    .hostImg(hostImgList)
//                    .region(host.getRegion())
//                    .gender(host.getGender())
//                    .age(host.getAge())
//                    .farmsts(host.getFarmsts())
//                    .shortintro(host.getShortintro())
//                    .intro(host.getIntro())
//                    .lat(host.getLat())
//                    .lng(host.getLng())
//                    .maxPpl(host.getMaxPpl())
//                    .apprvYn(host.getApprvYn())
//                    .build();
        }
        return hostSaveResponseDto;
    }

    // Host 데이터 + 메인이미지 등록
    @Override
    public String save(HostSaveRequestDto requestDto, MultipartFile file) {
        // 처음 등록이기 때문에 (update 시 role 이 admin 인경우에 Y로 변경)
        requestDto.setApprvYn("N");
        // 1. 호스트 정보에 대해서 등록한 후
        Host host = hostRepository.save(requestDto.toEntity());

        // 2. 그 번호를 가지고 이름을 임의로 지정한 후 저장
        String hostNum = String.valueOf(host.getHnum());
        String fileName = "["+hostNum+"]MainImg";
        File file1 = new File(filepath+fileName);
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final HostMainImg hostMainImg = HostMainImg.builder()
                .hnum(host.getHnum())
                .filename(fileName)
                .fileImgPath(filepath+fileName)
                .build();
        hostMainImgRepository.save(hostMainImg);

        // 3. 이미지 등록을 위해서 hostnum을 넘김
        return String.valueOf(hostNum);
    }

    // Host 이미지 등록 (각 파일마다 등록)
    @Override
    public void saveImgs(MultipartFile[] files, String hostNum) {
        Long hnum = Long.parseLong(hostNum);

        for (int i = 0; i < files.length; i++) {
            String fileName = "["+hostNum+"] images "+(i+1);
            File file1 = new File(filepath+fileName);
            try {
                files[i].transferTo(file1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            final HostImg img = HostImg.builder()
                    .hostImg_turn(Long.valueOf(i+1))
                    .hnum(hnum)
                    .filename(fileName)
                    .fileImgPath(filepath+fileName)
                    .build();
            hostImgRepository.save(img);
        }
    }


}
