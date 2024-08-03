package com.example.springboot.service.resrv;

import com.example.springboot.controller.dto.reserv.ResrvDscnResponseDto;
import com.example.springboot.domain.resrv.ResrvDscnRepository;
import com.example.springboot.domain.resrv.ResrvHis;
import com.example.springboot.domain.resrv.ResrvHisRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResrvServiceImpl implements ResrvService {
    ResrvDscnRepository resrvDscnRepository;
    ResrvHisRepository resrvHisRepository;
    @Override
    public List<ResrvDscnResponseDto> findResrvDscnByHost(String hnum) {
        List<ResrvDscnResponseDto> responseDtoList = new ArrayList<>();

        List<Long> resrvDscnResrvNumList = resrvDscnRepository.findResrvDscnByHnum(Long.valueOf(hnum));
        if (resrvDscnResrvNumList != null) {
            List<ResrvHis> resrvDscnByHumList = resrvHisRepository.findResrvHisByResrvNumIn(resrvDscnResrvNumList);
            responseDtoList = resrvDscnByHumList.stream().map(ResrvDscnResponseDto::new).collect(Collectors.toList());
        }
        return responseDtoList;
    }
}
