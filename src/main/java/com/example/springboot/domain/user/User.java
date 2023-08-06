package com.example.springboot.domain.user;

import com.example.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(unique = true)
    @NotNull
    private String email;

    @Column
    private String picture;

    @Column
    @NotNull
    private int status;

    @Enumerated(EnumType.STRING)
    @Column
    @NotNull
    private Role role;

    @Builder
    public User(String name, String email, String picture, int status, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.status = status;
        this.role = role;
    }

    // 회원정보 변경
    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    // 호스트 등록 변경
    public User stsUpdate(int status){
        this.status = status;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}