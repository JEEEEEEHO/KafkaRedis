package com.example.springboot.domain.cpn;

import com.example.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@Entity
@DynamicInsert
public class Cpn extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String cpnNum;


    @Column
    private Date regDt;

    @Column
    private String regId;

    @Column
    private int maxCnt;

    @Column
    private int ivtCnt;

    public void updateIvtCnt() {
        // 스레드 한개씩
        this.ivtCnt = ivtCnt -1;
    }
}