package com.example.springboot.domain.resrv;

import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resrv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resrvNum;

    @OneToOne
    @JoinColumn(name = "hnum")
    private Host host;

    @Column
    private String userid;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    /**
     * Y : 승락
     * D : 거절
     * N : 결정무
     * */
    @Column
    private String accptYn;

    // 예약마다 부여되는 인원
    @Column
    private String rsvdPpl;

}
