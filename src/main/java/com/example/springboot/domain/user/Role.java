package com.example.springboot.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "일반사용자"),
    USER("ROLE_USER", "멤버쉽"),
    ADMIN("ROLE_ADMIN", "관리자");
    private final String key;
    private final String title;

}
