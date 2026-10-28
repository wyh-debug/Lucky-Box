package com.luckybox.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.luckybox.constant.RedisConstants;
import com.luckybox.pojo.dto.CardDTO;
import com.luckybox.pojo.dto.RedisData;
import com.luckybox.pojo.dto.Result;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class CacheClient {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;

    private final Long RANDOM_ADD = 1000L;
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
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

    //逻辑过期
    public <ID, R>  R handleCacheBreakDown(String prefix, ID id, Class<?> clazz, Function<ID, R> dbFallback) {
        String key = prefix + id;
        //查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        if("NULL".equals(json)) {
            return null;
        }

        if(json == null || StrUtil.isBlank(json)) {
            RLock lock = redissonClient.getLock(RedisConstants.BOX_LOCK_KEY + id);
            boolean isLock = false;
            //从数据库找
            isLock = lock.tryLock();
            try {
                if(isLock) {
                    //拿到锁后查一次，可能刚写好
                    String retryJson = stringRedisTemplate.opsForValue().get(key);
                    if(!StrUtil.isBlank(retryJson)) {
                        if("NULL".equals(retryJson)) {
                            return null;
                        }
                        RedisData retryData = (RedisData) JSONUtil.toBean(retryJson, RedisData.class);
                        return (R) JSONUtil.toBean((JSONObject) retryData.getData(), clazz);
                    }
                    R r = dbFallback.apply(id);

                    if(r == null) {
                        stringRedisTemplate.opsForValue().set(key, "NULL", 1, TimeUnit.MINUTES);
                        return null;
                    }
                    this.setWithLogicalExpire(key, r, RedisConstants.CACHE_BOX_EXP, RedisConstants.CACHE_BOX_TTL, TimeUnit.MINUTES);
                    return r;
                }
            } finally {
                if(lock.isLocked() && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
            return null;

        }else{
            RedisData data = JSONUtil.toBean(json, RedisData.class);
            //逻辑过期
            if(LocalDateTime.now().isAfter(data.getExpire())) {
                //异步调用更新
                EXECUTOR_SERVICE.submit(()->{
                    RLock lock = redissonClient.getLock(RedisConstants.BOX_LOCK_KEY + id);
                    boolean isLock = false;
                    try {
                        isLock = lock.tryLock();
                        if(!isLock) {
                            return;
                        }
                        //拿到锁后查一次，可能刚写好
                        String latestJson = stringRedisTemplate.opsForValue().get(key);
                        if (StrUtil.isNotBlank(latestJson) && !"NULL".equals(latestJson)) {
                            RedisData latestData = JSONUtil.toBean(latestJson, RedisData.class);
                            if (latestData.getExpire().isAfter(LocalDateTime.now())) {
                                return; // 已被其他线程刷新，跳过
                            }
                        }
                        R r = dbFallback.apply(id);
                        if(r == null) {
                            stringRedisTemplate.opsForValue().set(key, "NULL", 1, TimeUnit.MINUTES);
                            return;
                        }
                        this.setWithLogicalExpire(key, r, RedisConstants.CACHE_BOX_EXP, RedisConstants.CACHE_BOX_TTL, TimeUnit.MINUTES);
                    }finally {
                        if(lock.isLocked() && lock.isHeldByCurrentThread()) {
                            lock.unlock();
                        }
                    }
                });
                R t = (R) BeanUtil.toBean(data.getData(), clazz);
                return t;
            }else {
                //没过期
                //不知道需不需要刷新逻辑过期时间？
                R r = (R) BeanUtil.toBean(data.getData(), clazz);
                return r;
            }
        }
    }

    /**
     * 防止缓存穿透，随机过期值
     * @return
     */
    public <ID, R> R cacheNullValue(String prefix, ID id, Class<R> clazz, Long timeout, TimeUnit unit, Function<ID, R> function) {
        String key = prefix + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        if("NULL".equals(json)) {
            return null;
        }
        if(StrUtil.isNotBlank(json)) {
            return JSONUtil.toBean(json, clazz);
        }
        RLock lock = redissonClient.getLock(RedisConstants.LOCK_CARD_KEY + id);
        boolean isLock = false;
        try{
            isLock = lock.tryLock();
            if(!isLock) {
                throw new Exception("系统繁忙稍后再试");
            }
            String retryJson = stringRedisTemplate.opsForValue().get(key);
            if ("NULL".equals(retryJson)) {
                return null;
            }
            if (StrUtil.isNotBlank(retryJson)) {
                return JSONUtil.toBean(json, clazz);
            }
            R r = function.apply(id);
            if(r == null) {
                //存空字符串
                stringRedisTemplate.opsForValue().set(key, "NULL", 5L, TimeUnit.MINUTES);
                return null;
            }
            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(r), timeout + RandomUtil.randomLong(RANDOM_ADD), unit);
            return r;

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(isLock && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
