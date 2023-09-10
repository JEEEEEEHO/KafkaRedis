package com.example.springboot.domain.host;

import lombok.*;

import javax.persistence.*;
@Getter // getter 메소드 생성
@Builder // 빌더를 사용할 수 있게 함
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class HostMainImg {
    @Id // primary key임을 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    @Column (nullable = false)
    private Long hnum;

    @Column(nullable = false, unique = true, length = 250)
    private String filename;

//    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
//    private String created_dt;
}
