package com.example.springboot.domain.wishlist;

import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.user.User;
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
    private Host hosts;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

}
