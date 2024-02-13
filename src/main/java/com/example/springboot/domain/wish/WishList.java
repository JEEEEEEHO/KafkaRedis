package com.example.springboot.domain.wish;

import com.example.springboot.domain.BaseTimeEntity;
import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishList extends BaseTimeEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishNum;

    @ManyToOne
    @JoinColumn(name = "hnum")
    private Host host;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;
}
