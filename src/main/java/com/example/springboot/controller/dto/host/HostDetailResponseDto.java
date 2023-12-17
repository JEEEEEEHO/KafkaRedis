package com.example.springboot.controller.dto.host;

import com.example.springboot.domain.host.HostImg;
import com.example.springboot.domain.host.HostMainImg;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostDetailResponseDto {
    private String hostNum;

    private HostMainImg hostMainImg;

    private List<HostImg> hostImg;

    private String region;

    private String gender;

    private String age;

    private String farmsts;

    private String shortintro;

    private String intro;

    private String address;

    private String maxPpl;
}
