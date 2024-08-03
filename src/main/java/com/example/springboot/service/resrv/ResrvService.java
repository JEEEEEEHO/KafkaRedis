package com.example.springboot.service.resrv;

import com.example.springboot.controller.dto.reserv.ResrvDscnResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResrvService {
    // 호스트 번호에 따른 예약 확정 정보 조회
    List<ResrvDscnResponseDto> findResrvDscnByHost(String hnum);
}
