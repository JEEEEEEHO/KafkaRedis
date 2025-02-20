package com.example.springboot.service.resrv;

import com.example.springboot.controller.dto.reserv.ResrvDscnResponseDto;
import com.example.springboot.controller.dto.reserv.ResrvHistRequestDto;
import com.example.springboot.domain.cpn.Cpn;
import com.example.springboot.domain.cpn.CpnIssu;
import com.example.springboot.domain.cpn.CpnIssuRepository;
import com.example.springboot.domain.cpn.CpnRepository;
import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostRepository;
import com.example.springboot.domain.resrv.*;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResrvServiceImpl implements ResrvService {
    ResrvDscnRepository resrvDscnRepository;
    ResrvHisRepository resrvHisRepository;
    UserRepository userRepository;
    HostRepository hostRepository;
    CpnRepository cpnRepository;
    CpnIssuRepository cpnIssuRepository;


    // 예약 요청 저장 TODO 임계영역 Redisson Lock
    @Override
    public void saveRequest(Principal principal, ResrvHistRequestDto histRequestDto) throws Exception {
        String userId = principal.getName();

        Optional<User> user = userRepository.findById(userId);
        Optional<Host> host = hostRepository.findById(Long.valueOf(histRequestDto.getHnum()));

        if(user.isPresent() && host.isPresent()){
            // 요청 인원
            int curReqPpl = histRequestDto.getReqPpl();

            // 호스트 재고 확인
            int curIvtPplByHost = hostRepository.countIvtPpl(host.get().getHnum());

            if (curReqPpl > curIvtPplByHost) {
                // 인원이 많으면 실패해야함
                throw new Exception("재고인원 부족");

            } else{
                // 호스트에서 재고 - reqPpl TODO MSA 고민
                host.get().updateIvtPpl(histRequestDto.getReqPpl());
                hostRepository.save(host.get());

                // 예약 완료
                histRequestDto.setUserid(userId);
                resrvHisRepository.save(histRequestDto.toEntity()); // JPA 는 트랜잭션 완료되었을 때, 변경된 데이터 모아 데이터 반영함

                // 쿠폰 발급 로직 - 같은 트랜잭션 TODO 카프카 생산 MSA
                // 선착순 20명 CPN00001
                Optional<Cpn> cpn = cpnRepository.findById("CPN00001");
                if (cpn.isPresent() && 0 >= cpn.get().getIvtCnt()) {
                    // 재고 존재
                    // 쿠폰 재고 차감
                    cpn.get().updateIvtCnt();
                    cpnRepository.save(cpn.get());

                    // 쿠폰 발급 테이블 저장
                    cpnIssuRepository.save(CpnIssu.builder()
                                                .cpnNum(cpn.get().getCpnNum())
                                                .userid(user.get().getId())
                                                .build()
                    );
                } else{
                    log.info("쿠폰 발급 실패 재고 부족");
                }
            }

        } else{
            throw new Exception("HOST OR USER is not found");
        }
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



    /**
     * 쓰지 않는 기능
     * */
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
                .restPpl(2)
                .build();

    }
}
