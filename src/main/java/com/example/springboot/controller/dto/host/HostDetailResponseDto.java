package com.example.springboot.controller.dto.host;

import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostImg;
import com.example.springboot.domain.host.HostMainImg;
import com.example.springboot.domain.resrv.ResrvHis;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostDetailResponseDto {
    // 호스트 정보
    private String hostNum;

    private String region;

    private String gender;

    private String age;

    private String farmsts;

    private String shortintro;

    private String intro;

    private String address;

    private String maxPpl;

    // 호스트 이미지
    private HostMainImg hostMainImg;

    private List<HostImg> hostImg;

    // 호스트 예약정보
    private  List<ResrvHis> resrvHisList;

    public HostDetailResponseDto(String hostNum, Host host, HostMainImg hostMainImg, List<HostImg> hostImgList){
        this.hostNum = hostNum;
        this.hostMainImg= hostMainImg;
        this.hostImg = hostImgList;
        this.region = host.getRegion();
        this.gender = host.getGender();
        this.age = host.getAge();
        this.farmsts = host.getFarmsts();
        this.shortintro = host.getShortintro();
        this.intro = host.getIntro();
        this.address = host.getAddress();
        this.maxPpl = String.valueOf(host.getMaxPpl());
    }

    // 예약정보 있는 경우
    public HostDetailResponseDto(String hostNum, Host host, HostMainImg hostMainImg, List<HostImg> hostImgList, List<ResrvHis> resrvHisList){
        this.hostNum = hostNum;
        this.hostMainImg= hostMainImg;
        this.hostImg = hostImgList;
        this.region = host.getRegion();
        this.gender = host.getGender();
        this.age = host.getAge();
        this.farmsts = host.getFarmsts();
        this.shortintro = host.getShortintro();
        this.intro = host.getIntro();
        this.address = host.getAddress();
        this.maxPpl = String.valueOf(host.getMaxPpl());
        this.resrvHisList = resrvHisList;
    }
}
