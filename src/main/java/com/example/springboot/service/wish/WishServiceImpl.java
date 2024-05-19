package com.example.springboot.service.wish;

import com.example.springboot.controller.dto.host.HostListResponseDto;
import com.example.springboot.controller.dto.wish.WishListRequestDto;
import com.example.springboot.controller.dto.wish.WishListResponseDto;
import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostRepository;
import com.example.springboot.domain.wish.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService{
    private final HostRepository hostRepository;
    private final WishRepository wishRepository;


    // INSERT
    @Override
    public void saveWish(java.lang.String userId, java.lang.String hnum) {
        Host host = hostRepository.findByHnum(Long.valueOf(hnum));
        WishListRequestDto requestDto = WishListRequestDto.builder()
                .userId(userId)
                .hostNum(host.getHnum())
                .host(host)
                .build();

        wishRepository.save(requestDto.toEntity());
    }

    // DELETE
    @Override
    public void deleteWish(String userId, String hnum) {
        wishRepository.deleteWishItem(userId,Long.valueOf(hnum));
    }

    // VIEW
    @Override
    public List<WishListResponseDto> viewWish(String userId) {
        return wishRepository.viewWish(userId);
    }
}
