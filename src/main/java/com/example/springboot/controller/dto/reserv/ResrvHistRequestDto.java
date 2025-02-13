package com.example.springboot.controller.dto.reserv;

import com.example.springboot.domain.resrv.AcceptStatus;
import com.example.springboot.domain.resrv.ResrvHis;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ResrvHistRequestDto {
    private String hnum;
    private String userid;
    private Date startDate;
    private Date endDate;
    private String reqPpl;

    public ResrvHis toEntity() {
        return ResrvHis.builder()
                .hnum(Long.valueOf(hnum))
                .userid(userid)
                .startDate(startDate)
                .endDate(endDate)
                .reqPpl(reqPpl)
                .accptYn(AcceptStatus.DEFAULT.getStatus())//디폴트
                .build();
    }

}
