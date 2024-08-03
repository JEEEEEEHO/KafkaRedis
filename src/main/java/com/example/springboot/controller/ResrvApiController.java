package com.example.springboot.controller;

import com.example.springboot.controller.dto.reserv.ResrvDscnResponseDto;
import com.example.springboot.service.resrv.ResrvService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ResrvApiController {
    private final ResrvService resrvService;

    /**
     * Request 예약 요청
     *
     * @return
     */
    @PostMapping("/api/resrv/{hunm}")
    public void requestResrv(Principal principal,  @PathVariable String hnum) {
        return resrvService.
    }


    /**
     * Response 호스트 별 예약 날짜
     * @return ResrvDscnDto - 시작, 종료일시
     */
    @GetMapping("/api/resrv/{hunm}")
    public List<ResrvDscnResponseDto> findResrvDscnByHost(@PathVariable String hnum) {
        return resrvService.findResrvDscnByHost(hnum);
    }
}
