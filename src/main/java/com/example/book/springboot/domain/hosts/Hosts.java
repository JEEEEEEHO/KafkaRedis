package com.example.book.springboot.domain.hosts;

import com.example.book.springboot.domain.BaseTimeEntity;
import com.example.book.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Hosts extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hnum;

    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    @Column(nullable = false)
    private int region;

    @Column(nullable = false)
    private int gender;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private int farmsts;

    @Column(nullable = false)
    private String shortintro;

    @Column(length = 1000, nullable = false)
    private String intro;

    @Column(nullable = false)
    private String lat;

    @Column(nullable = false)
    private String lng;

    @Builder
    public Hosts(int region, int gender, int age, int farmsts, String shortintro, String intro, String lat, String lng, User user){
        this.region = region;
        this.gender = gender;
        this.age = age;
        this.farmsts = farmsts;
        this.shortintro = shortintro;
        this.intro = intro;
        this.lat = lat;
        this.lng = lng;
        this.user = user;
    }

    // 수정용
    public void hostUpdate(int region, int gender, int age, int farmsts, String shortintro, String intro, String lat, String lng){
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
