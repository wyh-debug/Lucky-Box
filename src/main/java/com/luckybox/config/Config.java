package com.luckybox.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("${spring.minio.endpoint}")
    private String ENDPOINT;
    @Value("${spring.minio.accessKey}")
    private String ACCESS_KEY;
    @Value("${spring.minio.secretKey}")
    private String SECRET_KEY;
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(ENDPOINT).credentials(ACCESS_KEY, SECRET_KEY).build();
    }
}
