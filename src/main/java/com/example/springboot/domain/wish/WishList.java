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

    private String userId;

    // 1개의 호스트가 여러개의 위시리스트에 있을 수 있으므로
    @ManyToOne
    @JoinColumn(name = "hnum")
    private Host host;
}
