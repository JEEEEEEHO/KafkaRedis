package com.example.springboot.domain.host;

import com.example.springboot.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostImg extends BaseTimeEntity {
    @Id
    @Column(nullable = false)
    private int hnum;

    @OneToOne
    @MapsId //@Id로 지정한 컬럼에 @OneToOne과 같이 관계를 매핑
    @JoinColumn(name = "hnum")
    private Host host;

    @Column
    private String thumImg;

    @Column
    private String img1;

    @Column
    private String img2;

    @Column
    private String img3;

    @Column
    private String img4;

    @Column
    private String img5;


}
