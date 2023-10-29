package com.example.springboot.controller.dto.host;

import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.user.User;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostUpdateRequestDto {

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

    private Date apprv_date;

    private String hostDeleteMainImg; // 수정

    private String hostNum; // 수정


    public Host toEntity(){
        return Host.builder()
                .region(region)
                .gender(gender)
                .age(age)
                .farmsts(farmsts)
                .shortintro(shortintro)
                .intro(intro)
                .lat(lat)
                .lng(lng)
                .maxPpl(maxPpl)
                .apprv_date(apprv_date)
                .build();
    }
}
