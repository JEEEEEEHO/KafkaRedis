package com.example.springboot.controller.dto.reserv;

import com.example.springboot.domain.resrv.ResrvHis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ResrvDscnResponseDto {
    private Date startDate;
    private Date endDate;

    @Builder
    public ResrvDscnResponseDto(ResrvHis entity) {
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
    }

}
