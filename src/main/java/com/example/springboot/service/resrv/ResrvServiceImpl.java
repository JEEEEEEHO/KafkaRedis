package com.example.springboot.service.resrv;

import com.example.springboot.controller.dto.reserv.ResrvDscnResponseDto;
import com.example.springboot.controller.dto.reserv.ResrvHistRequestDto;
import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostRepository;
import com.example.springboot.domain.resrv.*;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResrvServiceImpl implements ResrvService {
    ResrvDscnRepository resrvDscnRepository;
    ResrvHisRepository resrvHisRepository;
    UserRepository userRepository;
    HostRepository hostRepository;


    // 예약 요청 저장
    @Override
    public void saveRequest(Principal principal, ResrvHistRequestDto histRequestDto) throws Exception {
        String userId = principal.getName();

        Optional<User> user = userRepository.findById(userId);
        Optional<Host> host = hostRepository.findById(Long.valueOf(histRequestDto.getHnum()));

        if(user.isPresent() && host.isPresent()){
            histRequestDto.setUserid(userId);
            resrvHisRepository.save(histRequestDto.toEntity());
        } else{
            throw new Exception("HOST OR USER is not found");
        }
    }

    // 예약 승낙
    @Transactional
    @Override
    public void acceptRequest(String resrvNum) {
        ResrvHis resrvHis = resrvHisRepository.findById(Long.valueOf(resrvNum)).orElseThrow(() -> new IllegalArgumentException("예약번호가 존재하지 않습니다"));

        // 현재 시작~종료일자 겹치는 예약 확정 데이터 있는지 확인하고
        // 리스트로 값을 뽑고
        // 그 뽑아온 값들에서 해당 요청에 해당하는 인원수를 각각 빼줌

        // 거기에 있는 요청인원 > 남아있는 인원 이면 안됨 => 오류 뱉음

        // history 테이블 예약 상태 변경
        resrvHis.update(AcceptStatus.ACCEPT.getStatus());


        // history 확정 테이블에 저장
        ResrvDscn resrvDscn = ResrvDscn.builder()
                .resrvNum(resrvHis.getResrvNum())
                .resrvHis(resrvHis)
                .hnum(resrvHis.getHnum())
                .restPpl()
                .build();

    }

    // 호스트별 확정 예약 조회
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
