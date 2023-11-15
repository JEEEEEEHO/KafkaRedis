package com.example.springboot.domain.resrv;

import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.user.User;

import javax.persistence.*;
import java.util.Date;

public class Resrv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String resrvNum;

    @OneToOne
    @JoinColumn(name = "hnum")
    private Host host;

    @Column
    private String userid;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @Column
    private String accptYn;

    @Column
    private String people;


}
