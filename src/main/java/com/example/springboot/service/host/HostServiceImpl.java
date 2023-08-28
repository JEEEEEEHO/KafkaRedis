package com.example.springboot.service.host;

import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostImg;
import com.example.springboot.domain.host.HostImgRepository;
import com.example.springboot.domain.host.HostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HostServiceImpl implements HostService {

    private final HostRepository hostRepository;
    private final HostImgRepository hostImgRepository;

    @Override
    public void save(HostSaveRequestDto requestDto, MultipartFile[] files) {
        // Host 등록
        Host host = hostRepository.save(requestDto.toEntity());


        // HostImg 등록
        HostImg hostImg = new HostImg();
        hostImg.setHostImg_turn(1L);
        hostImg.setHnum(host.getHnum());
        hostImg.setHost(host);
        requestDto.setHostImg(hostImg);
        // 임의로
        // HostImg : files 정보 담기 - for 문
        /**
         * 여기서 for 문으로 1부터 순번 매기고, name, size, path 담기
         *
         * */
//        if(files!=null){
//            for (int i = 0; i < files.length; i++) {
//                requestDto.getHostImg().setHostImg_turn(i);
//                requestDto.getHostImg().setFileName(files[i].getName);
//
//            }
        hostImgRepository.save(requestDto.toHostImg());
    }
}
