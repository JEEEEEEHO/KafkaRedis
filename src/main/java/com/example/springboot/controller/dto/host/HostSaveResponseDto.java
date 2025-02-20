package com.example.springboot.controller.dto.host;

import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostImg;
import com.example.springboot.domain.host.HostMainImg;
import com.example.springboot.domain.user.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostSaveResponseDto {
    private String hostNum;

    private HostMainImg hostMainImg;

    private List<HostImg> hostImg;

    private String region;

    private String gender;

    private String age;

    private String farmsts;

    private String shortintro;

    private String intro;

    private String address;


    private String maxPpl;

    private String apprvYn;

    public HostSaveResponseDto(String hostNum, Host host, HostMainImg hostMainImg, List<HostImg> hostImgList){
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
        this.apprvYn = host.getApprvYn();
    }
}
