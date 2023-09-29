package com.example.springboot.domain.host;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HostMainImgRepository extends JpaRepository<HostMainImg, Long> {
    // Host Main Img 찾기
    @Query("SELECT h FROM Host h WHERE h.hnum = ?1")
    HostMainImg findMainImg(Long hostNum);
}
