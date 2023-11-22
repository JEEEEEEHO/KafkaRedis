package com.example.springboot.domain.resrv;

import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResrvDscnRepository extends JpaRepository<ResrvDscn, Long> {
    // 예약확정된 호스트들의 목록을 구함
    @Query("SELECT d FROM ResrvDscn d WHERE d.resrvHis.host.hnum IN :#{#host.hnum} ")
    List<ResrvDscn> resrvDscnOfHosts (@Param("host") List<Host> hostList);
}
