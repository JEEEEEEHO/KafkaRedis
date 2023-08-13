package com.example.springboot.domain.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    // 소셜 로그인 반환 값 중 email을 통해 이미 생성된 사용자인지 판단
    Boolean existsByEmail(String email);
    User findByEmailAndPassword(String email, String password);
}
