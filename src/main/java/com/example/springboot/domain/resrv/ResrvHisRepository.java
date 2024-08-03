package com.example.springboot.domain.resrv;

import com.example.springboot.controller.dto.reserv.ResrvDscnResponseDto;
import com.example.springboot.domain.host.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ResrvHisRepository extends JpaRepository<ResrvHis, Long> {
    // 예약번호에 따른 시작, 종료 일자 조회
    List<ResrvHis> findResrvHisByResrvNumIn(List<Long> resrvDscnResrvNumList);
}
