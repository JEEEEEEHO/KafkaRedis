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
    private User user;

    private String region;

    private String gender;

    private String age;

    private String farmsts;

    private String shortintro;

    private String intro;

    private String lat;

    private String lng;

    private String maxPpl;

    private String apprvYn;

    private Date apprv_date;

    // 파일 삭제용
    private String deleteMainImg;


    public Host toEntity(){
        return Host.builder()
                .user(user)
                .region(region)
                .gender(gender)
                .age(age)
                .farmsts(farmsts)
                .shortintro(shortintro)
                .intro(intro)
                .lat(lat)
                .lng(lng)
                .maxPpl(maxPpl)
                .apprvYn(apprvYn)
                .apprv_date(apprv_date)
                .build();
    }
}
