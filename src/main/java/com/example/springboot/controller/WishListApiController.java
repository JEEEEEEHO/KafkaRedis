package com.example.springboot.controller;

import com.example.springboot.controller.dto.host.HostListResponseDto;
import com.example.springboot.controller.dto.wish.WishListResponseDto;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import com.example.springboot.service.wish.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class WishListApiController {

    private final WishService wishService;
    private final UserRepository userRepository;


    /**
     * Request 위시리스트 등록
     * @return String (fail false)
     * */
    @PostMapping("/api/wishList/save")
    public WishListResponseDto saveWishList(@RequestBody String hnum, Principal principal){
        WishListResponseDto wishListResponseDto = new WishListResponseDto();
        try {
            // token 값에 저장되어 있는 userId
            String userId = principal.getName();
            Optional<User> user = userRepository.findById(userId);
            if(user.isPresent()){
                // user id와 호스트 번호 넣기
               wishService.saveWish(userId, hnum);
               wishListResponseDto.setHostNum(hnum);
            }
        } catch (NullPointerException e){
            new Exception("No User : {}", e);
        }
        return wishListResponseDto;
    }

    /**
     * Request 위시리스트 삭제
     * @return String (fail false)
     * */
    @DeleteMapping("/api/wishList/delete")
    public void deleteWishList(@RequestBody String hnum , Principal principal){
        String userId = principal.getName();
        Optional<User> user = userRepository.findById(userId);
        try {
            // token 값에 저장되어 있는 userId
            if(user.isPresent()){
                // user id와 호스트 번호 넣기
                wishService.deleteWish(userId, hnum);
            }
        } catch (NullPointerException e){
            new Exception("No User : {}", e);
        }
    }

    /**
     * Response 위시리스트 전체 리스트
     * @return HostListResponseDto
     * */
    @GetMapping("/api/wishList/list")
    public List<HostListResponseDto> viewWishList(Principal principal) throws Exception {
        try {
            // token 값에 저장되어 있는 userId
            String userId = principal.getName();
            Optional<User> user = userRepository.findById(userId);
            if(user.isPresent()){
                // user id 에 해당하는 wishList에 호스트 목록 뽑기
                return wishService.viewWish(userId);
            }
        } catch (NullPointerException e){
            throw new Exception("No user");
        }
        return null;
    }
}

