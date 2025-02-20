package com.example.springboot.domain.cpn;

import com.example.springboot.domain.host.HostImpPk;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@IdClass(CpnIssuPk.class)
@Builder
public class CpnIssu implements Serializable {
    // 복합키
    @Id
    @Column
    private String cpnNum;

    @Id
    @Column(nullable = false)
    private String userid;
}
