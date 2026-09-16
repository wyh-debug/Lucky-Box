package com.wyh.luckybox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // 所有请求放行
                .csrf(csrf -> csrf.disable()) // 如果不需要 CSRF，可以禁用
                .formLogin(form -> form.disable()) // 禁用默认登录页
                .httpBasic(basic -> basic.disable()); // 禁用 HTTP Basic
        return http.build();
    }
}
