package com.example.springboot.domain.host;

import com.example.springboot.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@IdClass(HostImpPk.class)
@Builder
public class HostImg extends BaseTimeEntity implements Serializable {

    @Id
    @Column
    private Long hostImg_turn;

    @Id
    @Column(nullable = false)
    private Long hnum;

    @OneToOne
    @MapsId //@Id로 지정한 컬럼에 @OneToOne과 같이 관계를 매핑
    @JoinColumn(name = "hnum")
    private Host host;

    @Column
    private String fileName;

    @Column
    private String fileSize;

    @Column
    private String filePath;

    // 섬네일 이미지 여부
    @Column
    private String thumImg;

}
