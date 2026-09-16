package com.wyh.luckybox.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyh.luckybox.constant.SystemConstants;
import com.wyh.luckybox.mapper.ProductMapper;
import com.wyh.luckybox.mapper.ProductSkuMapper;
import com.wyh.luckybox.pojo.dto.PageDTO;
import com.wyh.luckybox.pojo.dto.ProductSelectDTO;
import com.wyh.luckybox.pojo.dto.RedisData;
import com.wyh.luckybox.pojo.dto.Result;
import com.wyh.luckybox.pojo.entity.Product;
import com.wyh.luckybox.service.IProductService;
import com.wyh.luckybox.utils.CacheClient;
import com.wyh.luckybox.utils.RegexUtils;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.wyh.luckybox.constant.RedisConstants.*;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {
   @Resource
   private StringRedisTemplate stringRedisTemplate;

   @Resource
   private CacheClient cacheClient;

   @Resource
   private RedissonClient redissonClient;

   @Resource
   private ProductMapper productMapper;

   @Resource
   private ProductSkuMapper productSkuMapper;

   private final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newSingleThreadExecutor();
    @Override
    public Result addProduct(Product product) {
        Boolean success = this.save(product);
        if(!success) {
            return Result.fail("添加商品失败");
        }
        cacheClient.setWithLogicalExpire(CACHE_PRODUCT_KEY + product.getId(), product, CACHE_PRODUCT_EXP, CACHE_PRODUCT_TTL, TimeUnit.MINUTES);
        return Result.ok();
    }


    @Override
    public Result updateProduct(Product product) {
        Boolean success = this.update(product, new LambdaUpdateWrapper<Product>().eq(Product::getId, product.getId()));
        if(!success) {
            return Result.fail("更新失败");
        }
        String key =  CACHE_PRODUCT_KEY + product.getId();
        stringRedisTemplate.delete(key);
        return Result.ok();
    }

    @Override
    public Result getProduct(Long id) throws InterruptedException {
        Product r = handleCacheBreakdown(id);
        return Result.ok(r);
    }

    @Override
    public Result getProductList(ProductSelectDTO productSelectDTO) {
        Page<Product> page = new Page<>(productSelectDTO.getPage(), productSelectDTO.getSize());
        boolean flagA = validateKey(productSelectDTO.getAttributes());
        boolean flagB = validateKey(productSelectDTO.getSpecifications());
        if(!(flagA && flagB)){
            throw new RuntimeException();
        }
        Page<Product> productList = productMapper.getProductList(page, productSelectDTO);
        PageDTO r = PageDTO.build(productList, Product.class);
        return Result.ok(r);
    }

    @Override
    public Result deleteProduct(Long id) {
        boolean exist = productSkuMapper.existsProductSku(id);
        if(exist) {
            return Result.fail("存在商品Sku，不能删除");
        }
        this.deleteProduct(id);
        stringRedisTemplate.delete(CACHE_PRODUCT_KEY + id);
        return Result.ok();
    }

    @Override
    public Result sortProduct(Integer sort, Integer pageNum, Integer pageSize) {
        if(pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if(pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if(SystemConstants.PRICE_UP == sort) {
            wrapper.orderByAsc(Product::getPrice);
        }
        if(SystemConstants.PRICE_DOWN == sort) {
            wrapper.orderByDesc(Product::getPrice);
        }
        if(SystemConstants.SALE_UP == sort) {
            wrapper.orderByAsc(Product::getSale);
        }
        PageDTO r = PageDTO.build(this.page(page, wrapper), Product.class);
        return Result.ok(r);
    }

    private boolean validateKey(Map<String, Object> map) {
        if(map == null || map.isEmpty()) {
            return true;
        }
        for(String s : map.keySet()) {
            if(!RegexUtils.isSQLKey(s)) {
                return false;
            }
        }
        return true;
    }

    private Product handleCacheBreakdown(Long id) throws InterruptedException {
        String key = CACHE_PRODUCT_KEY + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        if("NULL".equals(json)){
            return null;
        }
        String lockKey = PRODUCT_LOCK_KEY + id;
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLock = false;

        //查询key判断是否存在
        //        不存在，加锁，在数据库中查询，同时返回数据
        try {
            if(StrUtil.isBlank(json)) {
                isLock = lock.tryLock();
                if(isLock) {
                    Product p = this.getById(id);
                    if(Objects.isNull(p)) {
                        stringRedisTemplate.opsForValue().set(key, "NULL", 1, TimeUnit.MINUTES);
                        return null;
                    }
                    cacheClient.setWithLogicalExpire(key, p, CACHE_PRODUCT_EXP, CACHE_PRODUCT_TTL, TimeUnit.MINUTES);
                    return p;
                }else {
                    Thread.sleep(50);
                    return handleCacheBreakdown(id);
                }
            }
        }catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (isLock && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        //        存在：
        //        没超过逻辑过期时间：返回数据
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        if(LocalDateTime.now().isAfter(redisData.getExpire())) {
            isLock = lock.tryLock();
            if(isLock) {
                //			超过逻辑过期时间：调用另一个线程2在数据库中查询，同时返回旧数据
                CACHE_REBUILD_EXECUTOR.submit(() ->{
                    try {
                        Product p = this.getById(id);
                        if(Objects.isNull(p)) {
                            stringRedisTemplate.opsForValue().set(key, "NULL", 1, TimeUnit.MINUTES);
                        }
                        cacheClient.setWithLogicalExpire(key, p, CACHE_PRODUCT_EXP, CACHE_PRODUCT_TTL, TimeUnit.MINUTES);
                    }  finally {
                        if(lock.isHeldByCurrentThread()) {
                            lock.unlock();
                        }
                    }
                });
            }
            return JSONUtil.toBean((JSONObject) redisData.getData(), Product.class);
        }
        return JSONUtil.toBean((JSONObject) redisData.getData(), Product.class);
    }

}
