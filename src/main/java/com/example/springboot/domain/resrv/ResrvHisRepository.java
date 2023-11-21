package com.example.springboot.domain.resrv;

import com.example.springboot.domain.host.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ResrvHisRepository extends JpaRepository<ResrvHis, Long> {


}
