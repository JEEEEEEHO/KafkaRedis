package com.example.springboot.service.resrv;

import com.example.springboot.controller.dto.reserv.ResrvDscnResponseDto;
import com.example.springboot.controller.dto.reserv.ResrvHistRequestDto;
import com.example.springboot.domain.cpn.Cpn;
import com.example.springboot.domain.cpn.CpnIssu;
import com.example.springboot.domain.cpn.CpnIssuRepository;
import com.example.springboot.domain.cpn.CpnRepository;
import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostRepository;
import com.example.springboot.domain.resrv.*;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import com.example.springboot.exception.NoAvailableExeption;
import com.example.springboot.exception.NoLockException;
import com.example.springboot.exception.NoUserException;
import com.example.springboot.service.redis.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResrvServiceImpl implements ResrvService {
    @Autowired
    private ResrvDscnRepository resrvDscnRepository;
    @Autowired
    private ResrvHisRepository resrvHisRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private RedisService redisService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    // 예약 요청 저장 TODO 임계영역 Redisson Lock
    @Override
    public void saveRequest(Principal principal, ResrvHistRequestDto histRequestDto) throws Exception {
        String userId = principal.getName();

//         //ngrinder 용
//        String userId = "test";
//        histRequestDto.setUserid("test");
//        histRequestDto.setHnum("1");
//        histRequestDto.setReqPpl(2);

        Optional<User> user = userRepository.findById(userId);
        Optional<Host> host = hostRepository.findById(Long.valueOf(histRequestDto.getHnum()));

        if(user.isPresent()){
            // 요청 인원
            // Redisson 임계영역 시작
            String lockKey = "LockKey:" + userId;
            RLock lock = redissonClient.getLock(lockKey);
            try {
                boolean isLocked = lock.tryLock(3, 3, TimeUnit.SECONDS);
                if (!isLocked) {
                    throw new NoLockException();
                }

                int curReqPpl = histRequestDto.getReqPpl();
                long hnum = host.get().getHnum();
                int curIvtPplByHost = cahceCheck(hnum);;

                if (curReqPpl > curIvtPplByHost) {
                    // 3) 인원이 많으면 실패해야함
                    log.info("재고 부족으로 예약 실패");
                    throw new NoAvailableExeption(String.valueOf(hnum));

                } else{
                    // 4) redis 재고 차감 write through
                    redisService.deductAvailPpl(hnum, curReqPpl);
                    // 호스트 재고 차감
                    host.get().updateIvtPpl(curIvtPplByHost, histRequestDto.getReqPpl());
                    hostRepository.save(host.get());
                    log.info("host 예약 가능인원 차감 : {}", host.get().getIvtPpl());

                    // b) kafka 예약 히스토리 적재
                    // 예약 완료
                    histRequestDto.setUserid(userId);
                    String resrvObject = objectMapper.writeValueAsString(histRequestDto.toEntity());
                    kafkaTemplate.send("save_resrv_his", resrvObject);

                    // c) kafka 쿠폰 발급
                    kafkaTemplate.send("issue_coupon", userId);
                }
            } catch (Exception e) {
                log.info("예약 과정에서의 오류 : {}", e.getMessage());

            }finally {
                // 5) 락 반납
                lock.unlock();
            }

        } else{
            throw new NoUserException();
        }
    }

    private int cahceCheck(long hnum) {
        // 호스트 재고 확인
        // Look aside
        // 1) 캐시 먼저 확인
        int curIvtPplByHost = -1;
        if (redisService.findRedisKey(hnum)) {
            curIvtPplByHost = redisService.getAvailPpl(hnum);
            log.info("현재 호스트 redis 예약 재고 : {}", curIvtPplByHost);

        } else{
            // 2) 없다면 DB 조회
            curIvtPplByHost = hostRepository.countIvtPpl(hnum);// DB 조회
            redisService.setInitialPpl(hnum, curIvtPplByHost); // 최초 redis update
            log.info("현재 호스트 db 예약 재고 : {}", curIvtPplByHost);
        }
        return curIvtPplByHost;
    }

    // 호스트별 확정 예약 조회
    @Override
    public List<ResrvDscnResponseDto> findResrvDscnByHost(String hnum) {
        List<ResrvDscnResponseDto> responseDtoList = new ArrayList<>();

        List<Long> resrvDscnResrvNumList = resrvDscnRepository.findResrvDscnByHnum(Long.valueOf(hnum));
        if (resrvDscnResrvNumList != null) {
            List<ResrvHis> resrvDscnByHumList = resrvHisRepository.findResrvHisByResrvNumIn(resrvDscnResrvNumList);
            responseDtoList = resrvDscnByHumList.stream().map(ResrvDscnResponseDto::new).collect(Collectors.toList());
        }
        return responseDtoList;
    }



    /**
     * 쓰지 않는 기능
     * */
    // 예약 승낙
    @Transactional
    @Override
    public void acceptRequest(String resrvNum) {
        ResrvHis resrvHis = resrvHisRepository.findById(Long.valueOf(resrvNum)).orElseThrow(() -> new IllegalArgumentException("예약번호가 존재하지 않습니다"));

        // 현재 시작~종료일자 겹치는 예약 확정 데이터 있는지 확인하고
        // 리스트로 값을 뽑고
        // 그 뽑아온 값들에서 해당 요청에 해당하는 인원수를 각각 빼줌

        // 거기에 있는 요청인원 > 남아있는 인원 이면 안됨 => 오류 뱉음

        // history 테이블 예약 상태 변경
        resrvHis.update(AcceptStatus.ACCEPT.getStatus());


        // history 확정 테이블에 저장
        ResrvDscn resrvDscn = ResrvDscn.builder()
                .resrvNum(resrvHis.getResrvNum())
                .resrvHis(resrvHis)
                .hnum(resrvHis.getHnum())
                .restPpl(2)
                .build();

    }
}
