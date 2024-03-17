package com.example.springboot.service.wish;

import com.example.springboot.controller.dto.host.HostListResponseDto;

import java.util.List;

public interface WishService {
    // INSERT
    boolean saveWish(String userId, String hnum);

    // DELETE
    void deleteWish(String userId, String hnum);

    // VIEW
    List<HostListResponseDto> viewWish(String hnum);

}
