package com.example.book.springboot.web.dto.hosts;

import com.example.book.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
@Getter
@NoArgsConstructor
public class HostsSaveRequestDto {
    private int id;

    private int region;

    private int gender;

    private int age;

    private int farmsts;

    private String shortintro;

    private String intro;

    private String lat;

    private String lng;

    @Builder
    public HostsSaveRequestDto(int id, int region, int gender, int age, int farmsts, String shortintro, String intro, String lat, String lng){
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



}
