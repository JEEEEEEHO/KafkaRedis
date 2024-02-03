package com.example.springboot.service.wish;

import com.example.springboot.controller.dto.host.HostListResponseDto;

import java.util.List;

public interface WishService {
    // INSERT
    String saveWish(String hnum);

    // DELETE
    String deleteWish(String hnum);

    // VIEW
    List<HostListResponseDto> viewWish(String hnum);

}
