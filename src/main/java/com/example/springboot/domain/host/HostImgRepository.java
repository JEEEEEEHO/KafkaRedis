package com.example.springboot.domain.host;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HostImgRepository extends JpaRepository<HostImg, Long> {
    // Host 에 해당하는 모든 이미지 찾기
    @Query("SELECT h FROM HostImg h WHERE h.hnum= ?1")
    List<HostImg> findAllImgs(Long hostNum);
}
