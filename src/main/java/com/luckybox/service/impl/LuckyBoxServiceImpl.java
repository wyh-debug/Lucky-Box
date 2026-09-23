package com.luckybox.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.constant.RedisConstants;
import com.luckybox.mapper.*;
import com.luckybox.pojo.dto.*;
import com.luckybox.pojo.entity.LuckyBox;
import com.luckybox.pojo.entity.LuckyBoxCategory;
import com.luckybox.pojo.vo.LuckyBoxVO;
import com.luckybox.service.ILuckyBoxService;
import com.luckybox.utils.CacheClient;
import com.luckybox.utils.UserHolder;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class LuckyBoxServiceImpl extends ServiceImpl<LuckyBoxMapper, LuckyBox> implements ILuckyBoxService {
    @Resource
    private LuckyBoxCategoryMapper luckyBoxCategoryMapper;

    @Resource
    private LuckyBoxCardMapper luckyBoxCardMapper;
    @Resource
    private LuckyBoxOrderMapper luckyBoxOrderMapper;
    @Resource
    private LuckyBoxProductMapper luckyBoxProductMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private LuckyBoxMapper luckyBoxMapper;

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private CacheClient cacheClient;
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    @Override
    public Result addLuckyBox(LuckyBox luckyBox) {
        //查询是否存在分类
        boolean exists = luckyBoxCategoryMapper.exists(new LambdaQueryWrapper<LuckyBoxCategory>()
                .eq(LuckyBoxCategory::getId, luckyBox.getCategoryId()));
        if(!exists) {
            return Result.fail("分类不存在，无法添加");
        }
        boolean suc = this.save(luckyBox);
        if(!suc) {
            return Result.fail("添加失败");
        }
        return Result.ok();
    }

    @Override
    public Result deleteLuckyBox(Long id) {
        //			lucky_box_card-- `lucky_box_id`
        //			lucky_box_order--lucky_box_id`
        //lucky_box_product--lucky_box_id
        boolean flag1 = luckyBoxCardMapper.existBox(id);
        boolean flag2 = luckyBoxOrderMapper.existsBox(id);
        boolean flag3 = luckyBoxProductMapper.existsBox(id);
        //三个不存在删除
        if(flag1 || flag2 || flag3) {
            return Result.fail("无法删除");
        }
        boolean suc = this.removeById(id);
        if(!suc) {
            return Result.fail("删除失败");
        }
        //删除缓存
        stringRedisTemplate.delete(RedisConstants.CACHE_BOX_KEY + id);
        return Result.ok();
    }

    @Override
    public Result updateLuckyBox(LuckyBox luckyBox) {
        boolean suc = this.updateById(luckyBox);
        if(!suc) {
            return Result.fail("修改失败");
        }
        //删除缓存
        stringRedisTemplate.delete(RedisConstants.CACHE_BOX_KEY + luckyBox.getId());
        return Result.ok();
    }

    @Override
    public Result getLuckyBoxList(LuckyBoxSelectDTO luckyBoxSelectDTO) {
        //防止s'q'l注入
        String orderField = luckyBoxSelectDTO.getOrderField();
        String orderDirection = luckyBoxSelectDTO.getOrderDirection();
        //更新时间不想判断了
        if(orderField != null && !orderField.equals("price")) {
            return Result.fail("输入合理排序字段");
        }
        if(orderDirection != null && !(orderDirection.equals("ASC") || orderDirection.equals("DESC") || orderDirection.equals("asc") || orderDirection.equals("desc"))) {
            return Result.fail("输入正确排序方向");
        }
        Page<LuckyBoxVO> page = (Page<LuckyBoxVO>) luckyBoxSelectDTO.getPageQuery();
        page = luckyBoxMapper.getBoxList(page, luckyBoxSelectDTO);
        PageDTO dto = PageDTO.build(page, LuckyBoxVO.class);
        return Result.ok(dto);
    }

    @Override
    public Result getLuckyBox(Long id) {
        String key = RedisConstants.CACHE_BOX_KEY + id;
        //查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        if("NULL".equals(json)) {
            return Result.fail("盲盒不存在");
        }

        if(json == null || StrUtil.isBlank(json)) {
            RLock lock = redissonClient.getLock(RedisConstants.box_LOCK_KEY + id);
            boolean isLock = false;
            //从数据库找
            isLock = lock.tryLock();
            try {
                if(isLock) {
                    //拿到锁后查一次，可能刚写好
                    String retryJson = stringRedisTemplate.opsForValue().get(key);
                    if(!StrUtil.isBlank(retryJson)) {
                        if("NULL".equals(retryJson)) {
                            return Result.fail("盲盒不存在");
                        }
                        RedisData retryData = JSONUtil.toBean(retryJson, RedisData.class);
                        return Result.ok(JSONUtil.toBean((JSONObject) retryData.getData(), LuckyBox.class));
                    }
                    LuckyBox box = this.getById(id);
                    if(box == null) {
                        stringRedisTemplate.opsForValue().set(key, "NULL", 1, TimeUnit.MINUTES);
                        return Result.fail("盲盒不存在");
                    }
                    cacheClient.setWithLogicalExpire(key, box, RedisConstants.CACHE_BOX_EXP, RedisConstants.CACHE_BOX_TTL, TimeUnit.MINUTES);
                    return Result.ok(box);
                }
            } finally {
                if(lock.isLocked() && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
            return Result.fail("系统繁忙稍后再试");

        }else{
            RedisData data = JSONUtil.toBean(json, RedisData.class);
            //逻辑过期
            if(LocalDateTime.now().isAfter(data.getExpire())) {
                //异步调用更新
                EXECUTOR_SERVICE.submit(()->{
                    RLock lock = redissonClient.getLock(RedisConstants.box_LOCK_KEY + id);
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
                        LuckyBox box = this.getById(id);
                        if(box == null) {
                            stringRedisTemplate.opsForValue().set(key, "NULL", 1, TimeUnit.MINUTES);
                            return;
                        }
                        cacheClient.setWithLogicalExpire(key, box, RedisConstants.CACHE_BOX_EXP, RedisConstants.CACHE_BOX_TTL, TimeUnit.MINUTES);
                    }finally {
                        if(lock.isLocked() && lock.isHeldByCurrentThread()) {
                            lock.unlock();
                        }
                    }
                });
                LuckyBox luckyBox = BeanUtil.toBean(data.getData(), LuckyBox.class);
                return Result.ok(luckyBox);
            }else {
                //没过期
                //不知道需不需要刷新逻辑过期时间？
                LuckyBox luckyBox = BeanUtil.toBean(data.getData(), LuckyBox.class);
                return Result.ok(luckyBox);
            }
        }
    }

}
