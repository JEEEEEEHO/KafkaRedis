package com.example.springboot.security;

import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    public OAuthUserServiceImpl(){
        super();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        final OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();
        // 사용자 정보를 가져옴
        log.info("After Info : {}", attributes.toString());

        String email = null;
        String name = null;
        User user = null;

        String oauthType = userRequest.getClientRegistration().getRegistrationId();
        // getRegistrationId 현재 로그인 진행 중인 서비스를 구분하는 코드

        if("kakao".equals(oauthType.toLowerCase())){
            email = ((Map<String, Object>)attributes.get("kakao_account")).get("email").toString();
            name = ((Map<String, Object>)attributes.get("kakao_account")).get("nickname").toString();
        } else if ("google".equals(oauthType.toLowerCase())) {
            email = attributes.get("email").toString();
            name = attributes.get("name").toString();
        }

        if(!userRepository.existsByEmail(email)){
            // 유저가 존재하지 않다면
            user = User.builder()
                    .email(email)
                    .name(name)
                    .authProvider(oauthType)
                    .build();
            user = userRepository.save(user);
            // 저장함
        } else{
            user = userRepository.findByEmail(email);
            // 해당 user 뽑아냄
        }

        log.info("successfully pulled user info email{}", email);

        return oauth2User;
    }


}
