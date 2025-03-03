package com.example.springboot.domain.cpn;

import com.example.springboot.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
// Builder와 함께?
@AllArgsConstructor
@Builder

@DynamicInsert
public class Cpn extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cpnNum;

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