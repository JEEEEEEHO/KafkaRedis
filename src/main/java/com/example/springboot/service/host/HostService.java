package com.example.springboot.service.host;

import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.util.List;

public interface HostService {
    // 호스트 데이터 저장
    void save(HostSaveRequestDto dto, MultipartFile[] files);

}
