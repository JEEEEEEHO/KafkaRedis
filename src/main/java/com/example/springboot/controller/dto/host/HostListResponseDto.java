package com.example.springboot.controller.dto.host;

import com.example.springboot.domain.host.Host;
import lombok.Builder;
import lombok.Getter;

@Getter
public class HostListResponseDto {
    private int hnum;
    private String shortintro;
    private String mainImg;


    @Builder
    public HostListResponseDto(Host entity) {
        this.hnum = entity.getHnum();
        this.shortintro = entity.getShortintro();
        this.mainImg = entity.getMainImg();
    }
}
