package com.example.springboot.domain.cpn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CpnRepository extends JpaRepository<Cpn, String> {

    // 쿠폰을 가지고 있는 사용자인지 확인
    @Query("SELECT count(*) FROM CpnIssu c WHERE c.cpnNum= ?1 AND c.userid = ?2")
    int searchCpnByUserId(String cpnNum, String userId);

}
