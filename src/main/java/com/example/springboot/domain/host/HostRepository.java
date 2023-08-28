package com.example.springboot.domain.host;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HostRepository extends JpaRepository<Host, Long> {
    @Query("SELECT h FROM Host h ORDER BY h.hnum")
    List<Host> findAllDesc();

    @Query("SELECT h FROM Host h WHERE h.hnum = ?1")
    Host findByHnum(Long hostNum);
}
