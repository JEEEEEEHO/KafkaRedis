package com.example.springboot.config;

import com.example.springboot.security.JwtAuthenticationFilter;
import com.example.springboot.security.OAuthSuccessHandler;
import com.example.springboot.security.OAuthUserServiceImpl;
import com.example.springboot.security.RedirectUrlCookieFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationCodeGrantFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private OAuthUserServiceImpl oauthUserService; // 우리가 만든 OauthUserServiceImpl 추가
    @Autowired
    private OAuthSuccessHandler oAuthSuccessHandler;
    @Autowired
    private RedirectUrlCookieFilter redirectUrlCookieFilter;


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/img/**", "/css/**", "/js/**");    // /image/** 있는 모든 파일들은 시큐리티 적용을 무시한다.
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());    /* 정적인 리소스들에 대해서 시큐리티 적용 무시. */}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http 시큐리티 빌더
        http.cors() // WebMvcConfig에서 이미 설정했으므로 기본 cors 설정.
                .and()
                .csrf()// csrf는 현재 사용하지 않으므로 disable
                .disable()
                .httpBasic()// token을 사용하므로 basic 인증 disable
                .disable()
                .sessionManagement()  // session 기반이 아님을 선언
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests() // /와 /auth/** 경로는 인증 안해도 됨.
                .antMatchers("/", "/auth/**", "/api/**", "/oauth2/**").permitAll()
                .anyRequest() // /와 /auth/**이외의 모든 경로는 인증 해야됨.
                .authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login() // 로그인 페이지가 없을 땐 Spring Security 기본 OAuth 로그인 페이지
                .redirectionEndpoint()
                .baseUri("/login/oauth2/code/*")
                .and()
                .authorizationEndpoint()
                .baseUri("/auth/authorize") // 흐름 시작
                .and()
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler(oAuthSuccessHandler)// 성공하면 이 부분 실행
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint());

        // filter 등록.
        // 매 리퀘스트마다
        // CorsFilter 실행한 후에
        // jwtAuthenticationFilter 실행한다.
        http.addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class
        );

        http.addFilterBefore(
                redirectUrlCookieFilter,
                OAuth2AuthorizationCodeGrantFilter.class
                // 리다이렉트 되기 전에 필터를 실행해야 한다
        );
    }
}
