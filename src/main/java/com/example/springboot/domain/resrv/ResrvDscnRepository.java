package com.example.springboot.domain.resrv;

import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ResrvDscnRepository extends JpaRepository<ResrvDscn, Long> {
    // 예약확정된 호스트들의 목록을 구함
    @Query("SELECT d FROM ResrvDscn d WHERE d.resrvHis.host.hnum IN :#{#host.hnum} ")
    List<ResrvDscn> resrvDscnOfHosts (@Param("host") List<Host> hostList);

    // 요청하는 날짜에 예약이 있고, 요청인원 > 남은인원 인, 조건내에 예약을 받을 수 없는 예약확정들 조회
    @Query("SELECT d FROM ResrvDscn d WHERE :reqPpl > d.restPpl " +
            "AND d.resrvHis.startDate < :srchEndDate " +
            "OR d.resrvHis.endDate > :srchStartDate " )
    List<ResrvDscn> noAvailResrvDescn(@Param("srchStartDate") Date srchStartDate,
                                      @Param("srchEndDate") Date srchEndDate,
                                      @Param("reqPpl") int reqPpl);
}
