package com.example.springboot.service.kafka;

import com.example.demo.domain.OrdGod;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final OrdService ordService;


    @KafkaListener(topics = "set_ord", groupId = "ord")
    public void consume(List<OrdGod> ordGodList) {
        try {
            log.info("카프카 소비 주문 상품 리스트 역직렬화 : {}", ordGodList);
            ordService.saveOrd(ordGodList);

        } catch (Exception e) {
            e.printStackTrace();
            log.info(" 카프카 소비 실패 : {}", e.getMessage());

        }
    }
}
