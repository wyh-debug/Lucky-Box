package com.luckybox.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RedisData {
    private LocalDateTime expire;
    private Object data;
}
