package com.example.book.springboot.domain.review;

import com.example.book.springboot.domain.BaseTimeEntity;
import com.example.book.springboot.domain.hosts.Hosts;
import com.example.book.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int revnum;

    @Column(length = 500, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "hnum")
    private Hosts hosts;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    @Builder
    public Review (String content, Hosts hosts, User user){
        this.content = content;
        this.hosts = hosts;
        this.user = user;
    }

    public void reviewUpdate(String content){
        this.content = content;
    }
}
