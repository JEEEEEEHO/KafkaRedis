package com.example.springboot.security;

import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static com.example.springboot.security.RedirectUrlCookieFilter.REDIRECT_URI_PARAM;

@Slf4j
@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserRepository userRepository;

    public OAuthSuccessHandler(){
        super();
    }

    private static final String LOCAL_REDIRECT_URL = "http://localhost:3000";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        log.info("onAuthenticationSuccess auth succeeded");
        TokenProviderSns tokenProviderSns = new TokenProviderSns();
        String token = tokenProviderSns.create(authentication);

//        String email = null;
//        String oauthType = token.getAuthorizedClientRegistrationId();
//
//        if("kakao".equals(oauthType.toLowerCase())){
//            email = ((Map<String,Object>)token
//                    .getPrincipal().getAttributes().get("kakao_account")).get("email").toString();
//        } else if ("google".equals(oauthType.toLowerCase())) {
//            email = token.getPrincipal().getAttributes().get("email").toString();
//        }
//
//        log.info("Login Success : {}",email);
//
//        User user = userRepository.findByEmail(email);
//        // email로 user를 찾음
//
//        log.info("User Saved in Session");
//        HttpSession session = request.getSession();
//        session.setAttribute("user", user);

        Optional<Cookie> oCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(REDIRECT_URI_PARAM)).findFirst();
        Optional<String> redirectUri = oCookie.map(Cookie::getValue);

        log.info("token {}", token);
        response.sendRedirect(redirectUri.orElseGet(()->LOCAL_REDIRECT_URL)+"/sociallogin?token="+token);
    }

}
