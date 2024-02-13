package com.example.springboot.service.wish;

import com.example.springboot.controller.dto.host.HostListResponseDto;
import com.example.springboot.domain.wish.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService{
    private final WishRepository wishRepository;

    // INSERT
    @Override
    public String saveWish(String hnum) {
        return null;
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
