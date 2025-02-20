package com.example.springboot.controller.dto.host;

import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostMainImg;
import com.example.springboot.domain.wish.WishList;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class HostListResponseDto {
    private Long hnum;
    private String shortintro;
    private HostMainImg hostMainImg;


    @Builder
    public HostListResponseDto(Long hnum, String shortintro, HostMainImg hostMainImg) {
        this.hnum = hnum;
        this.shortintro = shortintro;
        this.hostMainImg = hostMainImg;

    }
}
