package com.example.springboot.controller.dto.host;

import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HostSaveRequestDto {
     private String id;

    private int region;

    private int gender;

    private int age;

    private int farmsts;

    private String shortintro;

    private String intro;

    private String lat;

    private String lng;

    @Builder
    public HostSaveRequestDto(String id, int region, int gender, int age, int farmsts, String shortintro, String intro, String lat, String lng){
        this.id = id;
        this.region = region;
        this.gender = gender;
        this.age = age;
        this.farmsts = farmsts;
        this.shortintro = shortintro;
        this.intro = intro;
        this.lat = lat;
        this.lng = lng;
    }

    public Host toEntity(User user){
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
                .build();
    }
}
