package com.example.springboot.controller.dto.host;

import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostImg;
import com.example.springboot.domain.user.User;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostSaveRequestDto {
    private User user;

    private String region;

    private String gender;

    private String age;

    private String farmsts;

    private String shortintro;

    private String intro;

    private String address;

    private String lat;

    private String lng;

    private String maxPpl;

    private String apprvYn;

    private Date apprv_date;


    public Host toEntity(){
        return Host.builder()
                .user(user)
                .region(region)
                .gender(gender)
                .age(age)
                .farmsts(farmsts)
                .shortintro(shortintro)
                .intro(intro)
                .address(address)
                .lat(lat)
                .lng(lng)
                .maxPpl(maxPpl)
                .restPpl(maxPpl)
                .apprvYn(apprvYn)
                .apprv_date(apprv_date)
                .build();
    }
}
