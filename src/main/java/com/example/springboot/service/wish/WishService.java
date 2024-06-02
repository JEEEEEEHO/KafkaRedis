package com.example.springboot.service.wish;

import com.example.springboot.controller.dto.wish.WishListResponseDto;

import java.util.List;

public interface WishService {
    // INSERT
    void saveWish(String userId, String hnum);

    // DELETE
    void deleteWish(String userId, String hnum);

    // VIEW
    List<WishListResponseDto> viewWish(String hnum);

}
