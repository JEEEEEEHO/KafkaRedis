package com.example.springboot.security;
import com.example.springboot.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProviderSns {
    private static final String SECRET_KEY = "Q4NSl604sgyHJj1qwEkR3ycUeR4uUAt7WJraD7EN3O9DVM4yyYuHxMEbSF4XXyYJkal13eqgB0F7Bq4HNn4d1MOVLZg79sfFACTIpCPKqWmpZMZQsbNrXdJJNWkRv50";


    public String create(final Authentication authentication) {
        log.info("TokenProviderSns");

        ApplicationOAuth2User userPrincipal = (ApplicationOAuth2User) authentication.getPrincipal();
        // 기한 지금으로부터 1일로 설정
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS));

        // JWT Token 생성
        byte[] keyBytes = SECRET_KEY.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(key, SignatureAlgorithm.HS512)
                // payload에 들어갈 내용
                .setSubject(userPrincipal.getName()) //
                .setIssuer("demo app") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate) // exp
                .compact();
    }
}
