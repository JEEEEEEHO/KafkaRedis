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


}
