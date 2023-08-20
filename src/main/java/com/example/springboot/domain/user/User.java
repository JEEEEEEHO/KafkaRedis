package com.example.springboot.domain.user;

import com.example.springboot.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User  extends BaseTimeEntity implements Serializable{

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id; // 유저에게 고유하게 부여되는 id.

    @Column(nullable = false)
    private String name; // 유저의 이름

    @Column(nullable = false)
    private String email; // 유저의 email, 아이디와 같은 기능을 한다.

    @Column
    private String password; // 패스워드. null이 가능한 이유는 oAuth로 페이스북이나 트위터같은 제3의 어플리케이션을 통해 로그인 할 수 있게 하기 위함이다.

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @Column
    private String authProvider; // 유저의 이름

}

