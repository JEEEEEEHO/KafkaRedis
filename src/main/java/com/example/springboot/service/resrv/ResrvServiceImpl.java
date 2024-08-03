package com.example.springboot.service.resrv;

import com.example.springboot.controller.dto.reserv.ResrvDscnResponseDto;
import com.example.springboot.domain.resrv.ResrvDscnRepository;
import com.example.springboot.domain.resrv.ResrvHis;
import com.example.springboot.domain.resrv.ResrvHisRepository;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResrvServiceImpl implements ResrvService {
    ResrvDscnRepository resrvDscnRepository;
    ResrvHisRepository resrvHisRepository;
    UserRepository userRepository;

    // 예약 요청 저장
    @Override
    public void saveRequest(Principal principal, String hnum) throws Exception {
        String userId = principal.getName();
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()){
            ResrvHis resrvHis = ResrvHis.builder()
                    .host()
                    .userid(user.get().getId())
                    .reqPpl("2")
                                .build();
        } else{
            throw new Exception("No User Info : forbidden access");
        }
    }

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
