package com.example.springboot.controller;

import com.example.springboot.controller.dto.host.HostListResponseDto;
import com.example.springboot.service.wish.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WishListApiController {

    private final WishService wishService;


    /**
     * Request 위시리스트 등록
     * @return String (fail false)
     * */
    @PostMapping("/api/wishList/save")
    public String saveWishList(String hnum){
        return wishService.saveWish(hnum);
    }

    /**
     * Request 위시리스트 삭제
     * @return String (fail false)
     * */
    @DeleteMapping("/api/wishList/delete")
    public String deleteWishList(String hnum){
        return wishService.deleteWish(hnum);
    }

    /**
     * Response 위시리스트 전체 리스트
     * @return HostListResponseDto
     * */
    @GetMapping("/api/wishList/list")
    public List<HostListResponseDto> viewWishList(String hnum){
        return wishService.viewWish(hnum);
    }
}

