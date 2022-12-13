package com.example.book.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    // 소셜 로그인 반환 값 중 email을 통해 이미 생성된 사용자인지 판단
}
