package com.example.springboot.service.resrv;

import com.example.springboot.controller.dto.reserv.ResrvDscnResponseDto;
import com.example.springboot.controller.dto.reserv.ResrvHistRequestDto;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface ResrvService {
    // 예약 요청 저장
    void saveRequest(Principal principal, ResrvHistRequestDto histRequestDto) throws Exception;

    // 예약 승낙
    void acceptRequest(String resrvNum);

    // 호스트 번호에 따른 예약 확정 정보 조회
    List<ResrvDscnResponseDto> findResrvDscnByHost(String hnum);
}
