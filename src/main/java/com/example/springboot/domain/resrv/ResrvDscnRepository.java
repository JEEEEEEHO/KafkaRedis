package com.example.springboot.domain.resrv;

import com.example.springboot.controller.dto.reserv.ResrvDscnResponseDto;
import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.user.User;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ResrvDscnRepository extends JpaRepository<ResrvDscn, Long> {
    // 예약확정된 호스트들의 목록을 구함
    @Query("SELECT rd.resrvHis.host.hnum FROM ResrvDscn rd WHERE rd.resrvHis.host.hnum IN :host")
    List<Long> resrvDscnHostList(@Param("host") List<Long> hostList);

    // 요청하는 날짜에 걸친 예약들 중에, 요청인원 > 남은 인원
    // 예약확정 테이블에 없다면 예약가능
    @Query("SELECT rd.resrvHis.host.hnum " +
            "FROM  ResrvDscn rd " +
            "WHERE rd.resrvHis.host.hnum IN :rdHost " +
            "AND :reqPpl > rd.restPpl " +
            "AND (rd.resrvHis.startDate < :srchEndDate OR rd.resrvHis.endDate > :srchStartDate )")
    List<Long> unAvailHostList ( @Param("srchStartDate") Date srchStartDate,
                                      @Param("srchEndDate") Date srchEndDate,
                                      @Param("reqPpl") int reqPpl,
                                      @Param("rdHost") List<Long> resrvDscnHostList);


    // 예약확정된 예약번호들을 구함
    List<Long> findResrvDscnByHnum(Long hum);

}
