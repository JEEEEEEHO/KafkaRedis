package com.example.springboot.service.kafka;

import com.example.springboot.domain.cpn.Cpn;
import com.example.springboot.domain.cpn.CpnIssu;
import com.example.springboot.domain.cpn.CpnIssuRepository;
import com.example.springboot.domain.cpn.CpnRepository;
import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostRepository;
import com.example.springboot.domain.resrv.ResrvHis;
import com.example.springboot.domain.resrv.ResrvHisRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {
    @Autowired
    private HostRepository hostRepository;
    @Autowired
    private ResrvHisRepository resrvHisRepository;
    @Autowired
    private CpnRepository cpnRepository;
    @Autowired
    private CpnIssuRepository cpnIssuRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @KafkaListener(topics = "save_resrv_his", groupId = "reserv")
    public void saveResrvHis(ConsumerRecord<String, String> record) {
        try {
            var resrvHis = objectMapper.readValue(record.value(), ResrvHis.class);
            resrvHisRepository.save(resrvHis); // JPA 는 트랜잭션 완료되었을 때, 변경된 데이터 모아 데이터 반영함
            log.info("카프카 소비 예약 히스토리 적재 : {}", resrvHis);

        } catch (Exception e) {
            e.printStackTrace();
            log.info(" 카프카 소비 예약 히스토리 적재 실패 : {}", e.getMessage());

        }
    }

    @KafkaListener(topics = "issue_coupon", groupId = "coupon")
    public void issueCoupon(String userId) {
        try {

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
                        .userid(userId)
                        .build()
                );
                log.info("사용자 쿠폰 발급 성공 : {}", cpn.get().getCpnNum());
            } else {
                log.info("쿠폰 발급 실패 재고 부족");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(" 카프카 소비 쿠폰 발급 실패 : {}", e.getMessage());
        }
    }
}
