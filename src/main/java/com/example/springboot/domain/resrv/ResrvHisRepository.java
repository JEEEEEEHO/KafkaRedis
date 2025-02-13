package com.example.springboot.domain.resrv;

import com.example.springboot.controller.dto.reserv.ResrvDscnResponseDto;
import com.example.springboot.domain.host.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ResrvHisRepository extends JpaRepository<ResrvHis, Long> {
    // 예약번호에 따른 시작, 종료 일자 조회를 위한 hist 목록 조회
    List<ResrvHis> findResrvHisByResrvNumIn(List<Long> resrvDscnResrvNumList);

    // 값 변경
    void updateResrvStatus(String resrvNum);
}
