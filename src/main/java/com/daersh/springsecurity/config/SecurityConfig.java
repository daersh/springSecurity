package com.daersh.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // securitY 커스터마이징
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception{
        /*
        * 인가 작업 커스터 마이징
        * permit all 로그인 없이 모두 접근 가능
        * hasRole 로그인 후 권한을 가지고 있는지
        * authorized 로그인 한 사람
        * denyAll 로그인 해도 접근 불가 모든 사용자 불가
        * hasAnyRole 여러 Role을 설정 가능
        *
        * requestMatchers: 경로 지정
        * anyRequest: 그 외 나머지 경로들 처리하는 것으로 최하단에 둘 것
        *
        *
        * 상단부터 검사하기 떄문에 경로 지정을 잘 해두자~~~~
         */
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/login").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN","USER")
                        .anyRequest().authenticated()
                );

        http.csrf(auth->auth.disable());

        // 접근 불가한 페이지 가면 자동으로 로그인 페이지 가도록 함
        http
                .formLogin(auth->auth
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .permitAll()
                        );

        return http.build();
    }


    //비밀번호 암호화

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){

        return new BCryptPasswordEncoder();
    }

}
