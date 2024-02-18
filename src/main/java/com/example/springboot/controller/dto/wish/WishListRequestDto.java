package com.example.springboot.controller.dto.wish;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishListRequestDto {
    List<Long> wishListHosts;
}
