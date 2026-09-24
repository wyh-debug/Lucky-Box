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
import java.util.function.Function;

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
        LuckyBox luckyBox = cacheClient.handleCacheBreakDown(RedisConstants.CACHE_BOX_KEY, id, LuckyBox.class, (boxId) -> this.getById(boxId));
        if(luckyBox == null) {
            return Result.fail("商品不存在");
        }
        return  Result.ok(luckyBox);
    }


}
