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

    private String maxPpl;

    private Date apprv_date;

    private String deleteMainImg; // 수정

    private String hostNum; // 수정

    private String[] deleteFiles; // 수정


}
