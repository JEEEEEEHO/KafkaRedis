package com.example.springboot.domain.wish;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<WishList, Long> {
}
