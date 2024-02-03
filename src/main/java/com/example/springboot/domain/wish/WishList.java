package com.example.springboot.domain.wish;

import com.example.springboot.domain.BaseTimeEntity;
import com.example.springboot.domain.host.Host;
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

    @OneToOne
    @JoinColumn(name = "hnum")
    private Host host;
}
