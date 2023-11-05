package com.example.springboot.service.host;

import com.example.springboot.controller.dto.host.HostListResponseDto;
import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.controller.dto.host.HostSaveResponseDto;
import com.example.springboot.controller.dto.host.HostUpdateRequestDto;
import com.example.springboot.domain.user.User;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.io.IOException;
import java.util.List;

public interface HostService {

    // 호스트 리스트 조회
    List<HostListResponseDto> findAllHost();

    // 호스트 데이터 불러오기
    HostSaveResponseDto findHostInfo(User user);

    // 호스트 데이터 저장
    String save(HostSaveRequestDto dto, MultipartFile file) throws IOException;

    // 호스트 이미지 저장
    void saveImgs(MultipartFile[] files, String hostNum) throws IOException;

    // 호스트 데이터 수정
    String update(HostUpdateRequestDto updateRequestDto, MultipartFile file) throws IOException;

    // 호스트 이미지 수정
    void updateImgs(MultipartFile[] files, String hostNum, HostUpdateRequestDto updateRequestDto) throws IOException;

}
