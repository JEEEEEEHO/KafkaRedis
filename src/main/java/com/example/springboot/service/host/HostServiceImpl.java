package com.example.springboot.service.host;

import com.example.springboot.controller.dto.host.*;
import com.example.springboot.domain.host.*;
import com.example.springboot.domain.resrv.ResrvDscnRepository;
import com.example.springboot.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class HostServiceImpl implements HostService  {
    public HostServiceImpl() throws IOException{};

    @Autowired
    private HostRepository hostRepository;
    @Autowired
    private HostMainImgRepository hostMainImgRepository;
    @Autowired
    private HostImgRepository hostImgRepository;
    @Autowired
    private ResrvDscnRepository resrvDscnRepository;


    /**
     * Response 호스트 전체 리스트
     * @return HostListResponseDto
     * */
    @Override
    public List<HostListResponseDto> findAllHost() {
        List<HostListResponseDto> list = new LinkedList<>();
        List<Host> hostList = hostRepository.findAll();
        for (Host host : hostList){
            HostMainImg hostMainImg = hostMainImgRepository.findMainImg(host.getHnum());
            list.add(new HostListResponseDto(host.getHnum(), host.getShortintro(), hostMainImg, host.getLat(), host.getLng()));
        }

        return list;
    }

    /**
     * Response 호스트 검색 searchHost +
     * @param HostsearchReqeustDto
     * @return List<HostListResponseDto>
     * */
    @Override
    public List<HostListResponseDto> searchHost(HostsearchReqeustDto hostsearchReqeustDto) throws ParseException {
        int reqPpl = Integer.parseInt(hostsearchReqeustDto.getPeople());
        String reqFrmst = hostsearchReqeustDto.getFarmsts();
        String reqGndr = hostsearchReqeustDto.getGender();
        String reqRegion = hostsearchReqeustDto.getRegion();

        if(reqGndr.isEmpty()){
            reqGndr = null;
        }
        if(reqFrmst.isEmpty()){
            reqFrmst = null;
        }
        if(reqRegion.isEmpty()){
            reqRegion = null;
        }

        List<HostListResponseDto> responseDtoList = new LinkedList<>();

        try{
            // 1. gender, region, status를 만족하는 Hostlist
            List<Host> hostListByOptions = hostRepository.hostListByOptions(reqFrmst,reqGndr,reqRegion, reqPpl, "Y");

            if(0 == hostListByOptions.size()){
                // 검색 조건에 만족하지 않은 경우
                responseDtoList.add(new HostListResponseDto(-1L, "", null, "", ""));
                return responseDtoList;
            }

            // 2. 조건에 만족한 호스트들 중에 예약 확정 테이블에 값이 존재하는 것들을 조회
            // 조회 쿼리에 맞게 사용하기 위해서 HOST 객체에서 num만을 따로 뽑아냄
            List<Long> hostListByOptionsToLong = new ArrayList<>();
            for (Host host : hostListByOptions){
                hostListByOptionsToLong.add(host.getHnum());
            }
            List<Long> resrvDscnHostList = resrvDscnRepository.resrvDscnHostList(hostListByOptionsToLong);

            if( 0 == resrvDscnHostList.size()){
                // 예약이 존재하지 않음 = 해당 호스트 모두 만족함
                for (Host host : hostListByOptions){
                    responseDtoList.add(new HostListResponseDto(host.getHnum(), host.getShortintro(), hostMainImgRepository.findMainImg(host.getHnum()), host.getLat(), host.getLng()));
                }
                return responseDtoList;
            }

            // 3. 해당 예약 확정들 중에서 찾는 날짜를 포함하고, 요청인원 > 수용인원을 넘어버린 예약확정들을 찾음
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMDD");
            String strStartDate = hostsearchReqeustDto.getStartDate();
            String strEndDate = hostsearchReqeustDto.getEndDate();

            Date srchStartDate = simpleDateFormat.parse(strStartDate);
            Date srchEndDate = simpleDateFormat.parse(strEndDate);

            List<Long> unAvailHostList = resrvDscnRepository.unAvailHostList(srchStartDate, srchEndDate, reqPpl, resrvDscnHostList);

            // 4. 1번 조건 만족 호스트  - 2번 예약확정된 호스트들을 제외시켜준 값 (최종)
            List<Host> srchdHostList = hostRepository.srchdHostList(hostListByOptionsToLong, unAvailHostList);

            for (Host host : srchdHostList){
                HostMainImg hostMainImg = hostMainImgRepository.findMainImg(host.getHnum());
                responseDtoList.add(new HostListResponseDto(host.getHnum(), host.getShortintro(), hostMainImg, host.getLat(), host.getLng()));
            }
        } catch (NullPointerException e) {
            // 검색 조건이 없을 떄
            responseDtoList.add(new HostListResponseDto(-1L, "", null, "", ""));
        }
        return responseDtoList;
    }


    /**
     * Response 호스트 상세보기
     * @param
     * @return
     * */

    @Override
    public HostDetailResponseDto viewHostDetail(String hnum) {
        Long hostNum = Long.valueOf(hnum);
        // 호스트 찾기
        Host host = hostRepository.findByHnum(hostNum);
        // 메인 이미지 찾기
        HostMainImg hostMainImg = hostMainImgRepository.findMainImg(hostNum);
        // 일반 이미지 리스트 찾기
        List<HostImg> hostImgList = hostImgRepository.findAllImgs(hostNum);

        // DTO 에 담기
        return new HostDetailResponseDto(hnum, host, hostMainImg, hostImgList);
    }


    /**
     * Response 호스트 내용 보기
     * @param user
     * @return HostSaveResponseDto
     * */
    @Override
    public HostSaveResponseDto findHostInfo(User user) {
        // userid로 host 정보를 찾음
        long count = hostRepository.findByUidCount(user);
        // Host 정보가 있다면
        if(count>0){
            Host host = hostRepository.findByUid(user);
            // Host 메인 이미지
            HostMainImg hostMainImg = hostMainImgRepository.findMainImg(host.getHnum());
            // Host 이미지 들
            List<HostImg> hostImgList = hostImgRepository.findAllImgs(host.getHnum());
            // DTO에 담는 부분

            return new HostSaveResponseDto(String.valueOf(host.getHnum()), host, hostMainImg, hostImgList);
        }
        // 해당하는 것이 없으면 빈 객체(생성자) 반환
        return new HostSaveResponseDto();
    }


    private final Path UPLOAD_PATH =
            Paths.get(new ClassPathResource("").getFile().getAbsolutePath() + File.separator + "static"  + File.separator + "image");

    /**
     * Request 호스트 등록(정보+메인이미지)
     * @param requestDto
     * @param file
     * @return String
     * */
    @Override
    public String save(HostSaveRequestDto requestDto, MultipartFile file) throws IOException {
        // 처음 등록이기 때문에 (update 시 role 이 admin 인경우에 Y로 변경)
         requestDto.setApprvYn("Y"); //- 테스트 용으로 잠시 Y
        // 1. 호스트 정보에 대해서 등록한 후
        Host host = hostRepository.save(requestDto.toEntity());

        // 2. 그 번호를 가지고 이름을 임의로 지정한 후 저장
        String hostNum = String.valueOf(host.getHnum());

        if(!Files.exists(UPLOAD_PATH)){
            // 경로가 존재하지 않는다면
            Files.createDirectories(UPLOAD_PATH);
        }

        String originFileName = file.getOriginalFilename();
            // 파일의 이름을 정함

        Path filepath = UPLOAD_PATH.resolve(originFileName);
        Files.copy(file.getInputStream(), filepath);
        // 파일에 있는 내용들을 출력하여 파일 path에 이름 하에 복사함

        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(originFileName).toUriString();

        final HostMainImg hostMainImg = HostMainImg.builder()
                .hnum(host.getHnum())
                .filename(originFileName)
                .fileUri(fileUri)
                .filepath(String.valueOf(filepath))
                .build();
        hostMainImgRepository.save(hostMainImg);

        // 3. 이미지 등록을 위해서 hostnum을 넘김
        return String.valueOf(hostNum);
    }

    /**
     * Request 호스트 등록(이미지)
     * @param files
     * @param hostNum
     * @return void
     * */
    @Override
    public void saveImgs(MultipartFile[] files, String hostNum) throws IOException {
        Long hnum = Long.parseLong(hostNum);

        for (int i = 0; i < files.length; i++) {

            String originFileName = files[i].getOriginalFilename();
            // 파일의 이름을 정함

            Path filepath = UPLOAD_PATH.resolve(originFileName);
            Files.copy(files[i].getInputStream(), filepath);
            // 파일에 있는 내용들을 출력하여 파일 path에 이름 하에 복사함

            String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(originFileName).toUriString();

            final HostImg img = HostImg.builder()
                    .hostImg_turn(Long.valueOf(i+1))
                    .hnum(hnum)
                    .filename(originFileName)
                    .fileUri(fileUri)
                    .filepath(String.valueOf(filepath))
                    .build();
            hostImgRepository.save(img);
        }
    }

    /**
     * Request 호스트 수정 (정보+메인이미지)
     * @param file
     * @param dto
     * @return String
     * */
    @Override
    public String update(HostUpdateRequestDto dto, MultipartFile file) throws IOException {
        // 1) DTO 에는 호스트 번호도 담고 있음 - 기존에 존재하는 host 수정
        Host host = hostRepository.findByHnum(Long.valueOf(dto.getHostNum()));
        host.updateHost(
                dto.getRegion()
                ,dto.getGender()
                ,dto.getAge()
                ,dto.getFarmsts()
                ,dto.getShortintro()
                ,dto.getIntro()
                ,dto.getAddress()
                ,dto.getLat()
                ,dto.getLng());
        hostRepository.save(host); // 수정

        // 2) 기존에 존재하는 파일 삭제 (수정이 불가능한 이유 : 파일명 등이 같을 수도 있음 -> 같은 경로에 파일이 생김)
        if(!dto.getDeleteMainImg().isEmpty() || !file.isEmpty()){
            // 해당 값이 존재하는 경우 기존의 값을 삭제
            hostMainImgRepository.delete(hostMainImgRepository.findMainImg(host.getHnum()));

            // 새로운 값을 등록
            String originFileName = file.getOriginalFilename();

            Path filepath = UPLOAD_PATH.resolve(originFileName);
            try{
                Files.copy(file.getInputStream(), filepath);
                // 수정시, 파일의 이름이 같은 경우, path가 같은 것으로 잡힘 => 에러날 가능성
            }catch (Exception e){
                throw new IOException("수정파일이름 동일 경로"+e);
            }

            String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(originFileName).toUriString();

            final HostMainImg hostMainImg = HostMainImg.builder()
                    .hnum(host.getHnum())
                    .filename(originFileName)
                    .fileUri(fileUri)
                    .filepath(String.valueOf(filepath))
                    .build();
            hostMainImgRepository.save(hostMainImg);
        }
        return String.valueOf(host.getHnum());
    }

    /**
     * Request 호스트 이미지 수정
     * @param files
     * @param hostNum
     * @param updateRequestDto
     * @return void
     * */
    @Override
    public void updateImgs(MultipartFile[] files, String hostNum, HostUpdateRequestDto updateRequestDto) throws IOException {
        Long hnum = Long.valueOf(hostNum);

        try {
            if(0 < updateRequestDto.getDeleteFiles().length ) {
                // 1개라도 있다면 이 부분
                for (String fileName : updateRequestDto.getDeleteFiles()) {
                    hostImgRepository.deleteImg(fileName);
                }

            }
            // 2) HostImg 의 가장 큰 turn 값을 찾아야 함 (내림차순 정렬)
            int maxTurn = 0; // 하나도 없는 경우 0에서 시작 (다 지워버렸거나)
            if (hostImgRepository.findAllImgs(hnum).size() > 0) {
                // 하나라도 있는 경우 마지막 순번
                maxTurn = (int) hostImgRepository.findLastImgTurn(hnum);
            }

            // 3) 새로운 Save
            for (int i = 0; i < files.length; i++) {
                String originFileName = files[i].getOriginalFilename();
                // 파일의 이름을 정함

                Path filepath = UPLOAD_PATH.resolve(originFileName);
                Files.copy(files[i].getInputStream(), filepath);
                // 파일에 있는 내용들을 출력하여 파일 path에 이름 하에 복사함

                String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(originFileName).toUriString();

                final HostImg img = HostImg.builder()
                        .hostImg_turn(Long.valueOf(i + 1 + maxTurn))
                        .hnum(hnum)
                        .filename(originFileName)
                        .fileUri(fileUri)
                        .filepath(String.valueOf(filepath))
                        .build();
                hostImgRepository.save(img);
            }
        } catch (NullPointerException e){
            log.debug("No delete imgs ", e);
            return;
        }
    }
}
