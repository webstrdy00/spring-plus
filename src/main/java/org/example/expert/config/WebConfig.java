package org.example.expert.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfig  {
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests
                        .requestMatchers("/auth/**").permitAll() // 로그인과 회원가입은 인증 없이 접근 가능
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()     // 그 외 모든 요청 인증처리
        );

        // 필터 관리
        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
