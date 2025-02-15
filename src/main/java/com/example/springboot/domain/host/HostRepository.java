package com.example.springboot.domain.host;

import com.example.springboot.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HostRepository extends JpaRepository<Host, Long> {
    // Host 리스트
    @Query("SELECT h FROM Host h ORDER BY h.hnum")
    List<Host> findAllDesc();

    // Host번호로 해당하는 Host 찾기
    @Query("SELECT h FROM Host h WHERE h.hnum = ?1")
    Host findByHnum(Long hostNum);

    // UserId로 해당하는 Host count
    @Query("SELECT COUNT(h) FROM Host h WHERE h.user.id = :#{#userInfo.id}")
    long findByUidCount(@Param("userInfo")User user);

    // UserId로 해당하는 Host 찾기
    @Query("SELECT h FROM Host h WHERE h.user.id = :#{#userInfo.id}")
    Host findByUid(@Param("userInfo")User user);

    // 검색조건 ( gender, farmsts + 승인된 호스트) 에 해당하는 Host 구하기
    @Query("SELECT h " +
            "FROM Host h " +
            "WHERE (:farmsts is null or h.farmsts = :farmsts) " +
            "AND (:gender is null or h.gender = :gender) " +
            "And (:region is null or h.region = :region)  " +
            "AND h.maxPpl >= :reqPpl  "+
            "AND h.apprvYn = :apprvYn")

    List<Host> hostListByOptions( @Param("farmsts") String farmsts,
                                    @Param("gender") String gender,
                                    @Param("region") String region,
                                    @Param("reqPpl") int reqPpl,
                                    @Param("apprvYn") String apprvYn);

    // 검색조건에 만족하는 호스트들 중에 - 예약이 불가능한 호스트 번호 = 예약이 가능한 호스트
    @Query(value = "SELECT h FROM Host h WHERE h.hnum IN :host AND NOT IN :resrvd", nativeQuery = true)
    List<Host> srchdHostList (@Param("host") List<Long> hostList,
                              @Param("resrvd") List<Long> unavailHostList);


    // 호스트 재고 확인
    @Query("SELECT h.ivtPpl FROM Host h WHERE h.hnum = ?1")
    int countIvtPpl (Long hostNum);
}
