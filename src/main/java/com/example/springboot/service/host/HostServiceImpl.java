package com.example.springboot.service.host;

import com.example.springboot.controller.dto.host.HostSaveRequestDto;
import com.example.springboot.controller.dto.host.HostSaveResponseDto;
import com.example.springboot.domain.host.*;
import com.example.springboot.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class HostServiceImpl implements HostService  {
    public HostServiceImpl() throws IOException{};

    @Autowired
    private HostRepository hostRepository;
    @Autowired
    private HostMainImgRepository hostMainImgRepository;
    @Autowired
    private HostImgRepository hostImgRepository;

    // Host 정보 불러오기
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

            return new HostSaveResponseDto(user, host, hostMainImg, hostImgList);
        }
        // 해당하는 것이 없으면 빈 객체(생성자) 반환
        return new HostSaveResponseDto();
    }


    private final Path UPLOAD_PATH =
            Paths.get(new ClassPathResource("").getFile().getAbsolutePath() + File.separator + "static"  + File.separator + "image");

    // Host 데이터 + 메인이미지 등록
    @Override
    public String save(HostSaveRequestDto requestDto, MultipartFile file) throws IOException {
        // 처음 등록이기 때문에 (update 시 role 이 admin 인경우에 Y로 변경)
        requestDto.setApprvYn("N");
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

    // Host 이미지 등록 (각 파일마다 등록)
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

    // Host 데이터 + 메인이미지 수정
    @Override
    public String update(HostSaveRequestDto dto, MultipartFile file) throws IOException {
        // DTO 에는 호스트 번호도 담고 있음 - 기존에 존재하는 host
        Host host = hostRepository.findByHnum(Long.valueOf(dto.getHostNum()));
        host.updateHost(dto.getRegion(),dto.getGender(), dto.getAge(), dto.getFarmsts(),
                dto.getShortintro(), dto.getIntro(),
                dto.getAddress(), dto.getLat(), dto.getLng());
        hostRepository.save(host); // 수정

        String originFileName = file.getOriginalFilename();

        Path filepath = UPLOAD_PATH.resolve(originFileName);
        try{
            Files.copy(file.getInputStream(), filepath);
            // 수정시, 파일의 이름이 같은 경우, path가 같은 것으로 잡힘 => 에러날 가능성
        }catch (Exception e){
            throw new IOException("수정파일이름 동일 경로"+e);
        }

        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(originFileName).toUriString();

        // 기존에 존재하는 hostMainImg
        HostMainImg hostMainImg = hostMainImgRepository.findMainImg(host.getHnum());
        hostMainImg.updateHostMainImg(originFileName, String.valueOf(filepath), fileUri);
        hostMainImgRepository.save(hostMainImg); // 수정

        return String.valueOf(host.getHnum());
    }

    // Host 이미지 수정 (각 파일마다 등록)
    @Override
    public void updateImgs(MultipartFile[] files, String hostNum, String[] deleteFiles) throws IOException {
        Long hnum = Long.valueOf(hostNum);

        // 1) fileName을 찾아서 지워줌
        for(String fileName : deleteFiles){
            hostImgRepository.deleteImg(fileName);
        }

        // 2) HostImg 의 가장 큰 turn 값을 찾아야 함 (내림차순 정렬)
        int maxTurn = (int) hostImgRepository.findLastImgTurn(hnum);


        // 3) 새로운 Save
        for (int i = 0; i < files.length; i++) {
            String originFileName = files[i].getOriginalFilename();
            // 파일의 이름을 정함

            Path filepath = UPLOAD_PATH.resolve(originFileName);
            Files.copy(files[i].getInputStream(), filepath);
            // 파일에 있는 내용들을 출력하여 파일 path에 이름 하에 복사함

            String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(originFileName).toUriString();

            final HostImg img = HostImg.builder()
                    .hostImg_turn(Long.valueOf(i+1+maxTurn))
                    .hnum(hnum)
                    .filename(originFileName)
                    .fileUri(fileUri)
                    .filepath(String.valueOf(filepath))
                    .build();
            hostImgRepository.save(img);
        }
    }


}
