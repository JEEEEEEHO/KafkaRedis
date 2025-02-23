package com.example.springboot.service.coupon;

import com.example.springboot.controller.dto.coupon.CouponInfoRequestDto;
import com.example.springboot.controller.dto.coupon.CouponInfoResponseDto;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService{
    UserRepository userRepository;
    @Override
    public CouponInfoResponseDto selectCouponByUser(CouponInfoRequestDto couponInfoRequestDto, Principal principal) {
        String userId = principal.getName();

        Optional<User> user = userRepository.findById(userId);

        return null;
    }
}
