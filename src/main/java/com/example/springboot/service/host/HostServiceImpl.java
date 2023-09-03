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
    public void save(HostSaveRequestDto requestDto) {
        // Host 등록
        Host host = hostRepository.save(requestDto.toEntity());

    }

    @Override
    public void saveImgs(MultipartFile[] files) {

    }
}
