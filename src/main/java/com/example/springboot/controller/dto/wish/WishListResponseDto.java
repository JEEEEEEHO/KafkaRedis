package com.example.springboot.controller.dto.wish;

import com.example.springboot.domain.wish.WishList;
import lombok.Builder;
import lombok.Getter;

@Getter
public class WishListResponseDto {
    private Long wishNum;
    private Long hostNum;

    @Builder
    public WishListResponseDto(WishList wishList) {
        this.wishNum = wishList.getWishNum();
        this.hostNum = wishList.getHostNum();
    }

}
