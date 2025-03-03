package com.example.springboot.service.coupon;

import com.example.springboot.controller.dto.coupon.CouponInfoRequestDto;
import com.example.springboot.controller.dto.coupon.CouponInfoResponseDto;

import java.security.Principal;

public interface CouponService {
    int selectCouponByUser(CouponInfoRequestDto couponInfoRequestDto, Principal principal);
}
