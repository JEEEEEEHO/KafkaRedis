package com.example.book.springboot.domain.wishlist;

import com.example.book.springboot.domain.hosts.Hosts;
import com.example.book.springboot.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wishnum;

    @ManyToOne
    @JoinColumn(name = "hnum")
    private Hosts hosts;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

}
