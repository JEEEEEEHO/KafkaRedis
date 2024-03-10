package com.example.springboot.service.wish;

import com.example.springboot.controller.dto.host.HostListResponseDto;
import com.example.springboot.controller.dto.wish.WishListRequestDto;
import com.example.springboot.domain.host.Host;
import com.example.springboot.domain.host.HostRepository;
import com.example.springboot.domain.wish.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService{
    private final HostRepository hostRepository;
    private final WishRepository wishRepository;

    // INSERT
    @Override
    public boolean saveWish(java.lang.String userId, java.lang.String hnum) {
        Host host = hostRepository.findByHnum(Long.valueOf(hnum));

        WishListRequestDto requestDto = WishListRequestDto.builder()
                .userId(userId)
                .host(host)
                .build();

        return wishRepository.save(requestDto.toEntity());
    }

    // DELETE
    @Override
    public String deleteWish(String hnum) {
        return null;
    }

    // VIEW
    @Override
    public List<HostListResponseDto> viewWish(String hnum) {
        return null;
    }
}
