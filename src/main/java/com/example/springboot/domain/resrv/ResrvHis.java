package com.example.springboot.domain.resrv;

import com.example.springboot.domain.host.Host;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResrvHis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resrvNum;

    @Column
    private Long hnum;

    @Column
    private String userid;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @Column
    private String accptYn;

    // 예약에 따른 요청 인원
    @Column
    private int reqPpl;

    @Column
    private Date acptdDate;

    public void update(String acceptYn) {
        this.accptYn = acceptYn;
    }


}
