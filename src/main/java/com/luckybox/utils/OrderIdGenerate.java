package com.luckybox.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class OrderIdGenerate {
    /**
     * 初始时间戳秒数
     */
    private static final long BEGIN_TIMESTAMP = 1704067200L;
    /**
     * 序列号位数
     */
    private static final int SEQUENCE_BITS = 32;

    @Resource
    private StringRedisTemplate  stringRedisTemplate;
    public long nextId(String prefix) {
        //key时间戳
        LocalDateTime now = LocalDateTime.now();
        long timestamp = now.toEpochSecond(ZoneOffset.UTC) - BEGIN_TIMESTAMP;
        String key = "icr: "+ prefix + now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        long sequence = stringRedisTemplate.opsForValue().increment(key);
        return timestamp << SEQUENCE_BITS | sequence;

    }
}
