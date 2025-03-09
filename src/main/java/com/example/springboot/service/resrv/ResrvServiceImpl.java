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
import com.example.springboot.exception.NoUserException;
import com.example.springboot.service.redis.RedisService;
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
    private CpnRepository cpnRepository;
    @Autowired
    private CpnIssuRepository cpnIssuRepository;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


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
                int curReqPpl = histRequestDto.getReqPpl();
                long hnum = host.get().getHnum();

                // 호스트 재고 확인
                // Look aside
                // 1) 캐시 먼저 확인
                int curIvtPplByHost = redisService.getAvailPpl(hnum);
                if (curIvtPplByHost == 0) {
                    // 2) 없다면 DB 조회
                    curIvtPplByHost = hostRepository.countIvtPpl(hnum);// DB 조회
                    redisService.setInitialPpl(hnum, curIvtPplByHost); // 최초 redis update
                }
                log.info("현재 호스트 예약 재고 : {}", curIvtPplByHost);

                if (curReqPpl > curIvtPplByHost) {
                    // 인원이 많으면 실패해야함
                    log.info("재고 부족으로 예약 실패");
                    throw new NoAvailableExeption(String.valueOf(hnum));

                } else{
                    // redis 재고 차감
                    redisService.deductAvailPpl(hnum, curReqPpl);
                    // 1) kafka 호스트 재고 차감
                    // 2) kafka 예약 히스토리 적재
                    // 3) kafka 쿠폰 발급

                    host.get().updateIvtPpl(histRequestDto.getReqPpl());
                    hostRepository.save(host.get());

                    // 예약 완료
                    histRequestDto.setUserid(userId);
                    resrvHisRepository.save(histRequestDto.toEntity()); // JPA 는 트랜잭션 완료되었을 때, 변경된 데이터 모아 데이터 반영함
                    log.info("사용자 예약 완료 / 현재 재고: {}",  host.get().getIvtPpl());


                    // 쿠폰 발급 로직 - 같은 트랜잭션 TODO 카프카 생산 MSA
                    // 선착순 20명 CPN00001
                    Optional<Cpn> cpn = cpnRepository.findById(1L);
                    // ** 회원이 쿠폰 가지고 있는지
                    int couponCntByUser = cpnRepository.searchCpnByUserId(1L, userId);
                    log.info("사용자 쿠폰 확인 조회 : {}", couponCntByUser);

                    if (cpn.isPresent() && cpn.get().getIvtCnt() > 0 && couponCntByUser <= 0) {
                        // 재고 존재 + 사용자 발급받은 적 없음

                        // 쿠폰 재고 차감
                        cpn.get().updateIvtCnt();
                        cpnRepository.save(cpn.get());
                        log.info("쿠폰 재고 차감 후 현재 재고: {}", cpn.get().getIvtCnt());

                        // 회원에 따른 쿠폰 발급
                        cpnIssuRepository.save(CpnIssu.builder()
                                .cpnNum(cpn.get().getCpnNum())
                                .userid(user.get().getId())
                                .build()
                        );
                        log.info("사용자 쿠폰 발급 성공 : {}", cpn.get().getCpnNum());
                    } else {
                        log.info("쿠폰 발급 실패 재고 부족");
                    }
                }
            } catch (Exception e) {

            }finally {
                lock.unlock();
            }


        } else{
            throw new NoUserException();
        }
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
