package com.example.springboot.service.host;

import com.example.springboot.controller.dto.host.HostListResponseDto;

import java.util.List;

public interface HostService {
    List<HostListResponseDto> findAllDesc();
}
