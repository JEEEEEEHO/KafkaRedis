package com.example.book.springboot.web.dto.hosts;

import com.example.book.springboot.domain.hosts.Hosts;
import com.example.book.springboot.domain.hosts.HostsRepository;
import com.example.book.springboot.domain.user.User;
import com.example.book.springboot.domain.user.UserRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
@Getter
@NoArgsConstructor
public class HostsSaveRequestDto {
     private Long id;

    private int region;

    private int gender;

    private int age;

    private int farmsts;

    private String shortintro;

    private String intro;

    private String lat;

    private String lng;

    @Builder
    public HostsSaveRequestDto(Long id, int region, int gender, int age, int farmsts, String shortintro, String intro, String lat, String lng){
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

    public Hosts toEntity(User user){
        return Hosts.builder()
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
