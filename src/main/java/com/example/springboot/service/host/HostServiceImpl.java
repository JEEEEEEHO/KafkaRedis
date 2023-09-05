package com.example.springboot.service.host;

import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.domain.host.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HostServiceImpl implements HostService {

    private final HostRepository hostRepository;
    private final HostMainImgRepository hostMainImgRepository;
    private final HostImgRepository hostImgRepository;
    String filepath = "/src/main/resources/static/img/Host/";

    @Override
    public void save(HostSaveRequestDto requestDto, MultipartFile file) {
        // Host 데이터 등록
        Host host = hostRepository.save(requestDto.toEntity());
        // 1. 호스트 정보에 대해서 등록한 후

        // 2. 그 번호를 가지고 이름을 임의로 지정한 후 저장
        Long hostNum = host.getHnum();
        String fileName = "["+hostNum+"] MainImg";
        long fileSize = file.getSize();
        File file1 = new File(filepath+fileName);
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final HostMainImg hostMainImg = HostMainImg.builder()
                .filename(fileName)
                .build();
        hostMainImgRepository.save(hostMainImg);
    }

    @Override
    public void saveImgs(MultipartFile[] files) {
        // Host 이미지 등록

    }
}
