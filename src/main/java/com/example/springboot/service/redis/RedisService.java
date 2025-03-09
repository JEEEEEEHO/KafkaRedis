package com.example.springboot.service.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 1. 최초 호스트당 예약 가능 인원 적재
    public void setInitialPpl(Long hnum, int ivtQty) {
        redisTemplate.opsForValue().set("HostNum:" + hnum, String.valueOf(ivtQty));
    }

    // 2. 호스트당 예약 가능 인원 업데이트
    public void updateAvailPpl(Long hnum, int ivtQty) {
        String key = "HostNum:" + hnum;
        redisTemplate.opsForValue().set(key, String.valueOf(ivtQty));
    }

    // 3. 호스트당 예약 가능 인원 확인
    public int getAvailPpl(Long hnum) {
        return Integer.valueOf(redisTemplate.opsForValue().get("HostNum:" + hnum));
    }

    // 4. 호스트당 예약 가능 인원 차감
    public boolean deductAvailPpl(Long hnum, int reqPpl) {
        String key = "HostNum:" + hnum;
        int curAvailPpl = Integer.valueOf(redisTemplate.opsForValue().get(key));
        if (curAvailPpl >= reqPpl) {
            redisTemplate.opsForValue().set(key, String.valueOf(curAvailPpl - reqPpl));
            log.info("redis 예약가능 인원 차감  {} : {} ->{}", key, curAvailPpl, curAvailPpl-reqPpl);
            return true;
        } else {
            log.info("redis 예약가능 인원 차감 실패 {} : {} < {}", key, curAvailPpl, reqPpl);
            return false;
        }
    }
}
