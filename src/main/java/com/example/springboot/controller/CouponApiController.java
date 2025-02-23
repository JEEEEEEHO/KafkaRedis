package com.example.springboot.controller;

import com.example.springboot.controller.dto.coupon.CouponInfoRequestDto;
import com.example.springboot.controller.dto.coupon.CouponInfoResponseDto;
import com.example.springboot.controller.dto.reserv.ResrvDscnResponseDto;
import com.example.springboot.controller.dto.reserv.ResrvHistRequestDto;
import com.example.springboot.service.coupon.CouponService;
import com.example.springboot.service.resrv.ResrvService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CouponApiController {

    private final CouponService couponService;

    /**
     * Request 쿠폰 조회
     *
     * @return
     */
//    @GetMapping("/api/coupon/{couponNum}")
//    public void selectCouponInfo(@RequestBody ResrvHistRequestDto histRequestDto, Principal principal) throws Exception {
//        resrvService.saveRequest(principal, histRequestDto);
//    }

    /**
     * Request 회원별 쿠폰 정보 조회
     *
     * @return
     */
    @GetMapping("/api/coupon/list")
    public CouponInfoResponseDto selectCouponByUser(@RequestBody CouponInfoRequestDto couponInfoRequestDto, Principal principal) throws Exception {
        return couponService.selectCouponByUser(couponInfoRequestDto, principal);
    }
}
