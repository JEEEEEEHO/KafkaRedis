package com.example.springboot.controller.dto.host;

import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostMainImg;
import lombok.Builder;
import lombok.Getter;

@Getter
public class HostListResponseDto {
    private Long hnum;
    private String shortintro;
    private HostMainImg hostMainImg;

    @Builder
    public HostListResponseDto(Host host, HostMainImg hostMainImg ){
        this.hnum = host.getHnum();
        this.shortintro = host.getShortintro();
        this.hostMainImg = hostMainImg;
    }
}
