package com.wyh.luckybox.config;

import com.wyh.luckybox.interceptor.LoginInterceptor;
import com.wyh.luckybox.interceptor.RefreshTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).order(0);
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/auth/**",
                        "/productCategory/list",
                        "/productCategory/page",
                        "/product/{id}",
                        "/product/list",
                        "/productSku/pro/{proId}",
                        "/productSku/{id}"
                ).order(1);
    }
}
