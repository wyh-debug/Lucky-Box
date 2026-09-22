package com.luckybox.utils;

import cn.hutool.json.JSONUtil;
import com.luckybox.pojo.dto.RedisData;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class CacheClient {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 逻辑保存数据到redis
     * @param key
     * @param obj
     * @param expire
     * @param timeout
     * @param unit
     */
    public void setWithLogicalExpire(String key, Object obj, Long expire, Long timeout, TimeUnit unit) {
        RedisData redisData = new RedisData();
        redisData.setExpire(LocalDateTime.now().plusMinutes(expire));
        redisData.setData(obj);
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData), timeout, unit);
    }
}
