package com.example.springboot.controller;

import com.example.springboot.controller.dto.reserv.ResrvDscnResponseDto;
import com.example.springboot.controller.dto.reserv.ResrvHistRequestDto;
import com.example.springboot.service.resrv.ResrvService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/api/resrv/save")
    public void requestResrv(Principal principal, @RequestBody ResrvHistRequestDto histRequestDto) throws Exception {
        resrvService.saveRequest(principal, histRequestDto);
    }


    /**
     * Request 예약 승낙
     * */
    @PostMapping("/api/resrv/accept")
    public void acceptResrv(String resrvNum) throws Exception {
        resrvService.acceptRequest(resrvNum);

    }

    /**
     * Response 예약 거절
     * */


    /**
     * Response 호스트 별 예약 날짜
     * @return ResrvDscnDto - 시작, 종료일시
     */
    @GetMapping("/api/resrv/{hunm}")
    public List<ResrvDscnResponseDto> findResrvDscnByHost(@PathVariable String hnum) {
        return resrvService.findResrvDscnByHost(hnum);
    }
}
