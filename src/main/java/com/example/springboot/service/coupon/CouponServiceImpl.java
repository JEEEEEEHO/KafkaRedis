package com.example.springboot.service.coupon;

import com.example.springboot.controller.dto.coupon.CouponInfoRequestDto;
import com.example.springboot.controller.dto.coupon.CouponInfoResponseDto;
import com.example.springboot.domain.cpn.CpnRepository;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service

@Slf4j
public class CouponServiceImpl implements CouponService{
    @Autowired
    private CpnRepository cpnRepository;

    @Override
    public int selectCouponByUser(CouponInfoRequestDto couponInfoRequestDto, Principal principal) {
        String userId = principal.getName();
        return cpnRepository.searchCpnByUserId(1L, userId);
    }
}
