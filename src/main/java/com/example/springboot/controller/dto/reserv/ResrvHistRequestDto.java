package com.example.springboot.controller.dto.reserv;

import com.example.springboot.domain.resrv.AcceptStatus;
import com.example.springboot.domain.resrv.ResrvHis;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResrvHistRequestDto {
    private String hnum;
    private String userid;
    private int reqPpl;
    private Date startDate;
    private Date endDate;

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
