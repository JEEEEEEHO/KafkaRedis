package com.example.springboot.domain.resrv;

import com.example.springboot.domain.host.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ResrvRepository extends JpaRepository<Resrv, Long> {
    // 1. HostList의 값을 가지고 있는 예약들
    List<Resrv> findResrvsByHostIn(List<Host> hostList);

    // 2. 예약 여부가 Y인 예약들을 구함
    List<Resrv> findResrvByAccptYnIs(String acceptYn);

    // 해당 시간내에 예약이 없는 예약들을 추림
    @Query("")
    List<Resrv> searchResrvByNoBooked(String acceptYn, Date startDate, Date endDate);

}
