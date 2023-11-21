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
public class ResrvDscn {
    @Id
    @OneToOne
    @JoinColumn(name = "resrvNum")
    private ResrvHis resrvHis;

    @Column
    private int restPpl;

    @Column
    private Date acptdDate;
}
