package com.example.springboot.controller.dto.wish;

import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.wish.WishList;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishListRequestDto {
    String userId;
    Long hostNum;
    Host host;

    public WishList toEntity(){
        return WishList.builder()
                .userId(userId)
                .hostNum(hostNum)
                .host(host)
                .build();
    }
}
