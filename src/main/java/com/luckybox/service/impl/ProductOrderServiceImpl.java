package com.luckybox.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.constant.RedisConstants;
import com.luckybox.mapper.BaseOrderMapper;
import com.luckybox.mapper.ProductOrderMapper;
import com.luckybox.mapper.ProductSkuMapper;
import com.luckybox.pojo.dto.OrderDTO;
import com.luckybox.service.IProductOrderService;
import com.luckybox.constant.SystemConstants;
import com.luckybox.mapper.AddressMapper;
import com.luckybox.pojo.dto.AddressDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.Address;
import com.luckybox.pojo.entity.BaseOrder;
import com.luckybox.pojo.entity.ProductOrder;
import com.luckybox.pojo.entity.ProductSku;
import com.luckybox.utils.OrderIdGenerate;
import com.luckybox.utils.UserHolder;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Objects;

@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrder> implements IProductOrderService {
    @Resource
    private ProductSkuMapper productSkuMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private OrderIdGenerate orderIdGenerate;
    @Resource
    private AddressMapper addressMapper;
    @Resource
    private BaseOrderMapper baseOrderMapper;
    @Resource
    private ProductOrderMapper productOrderMapper;
    @Resource
    private RedissonClient redissonClient;
    @Override
    @Transactional
    public Result addProductOrder(OrderDTO orderDTO) {

        Long skuId = orderDTO.getSkuId();
        ProductSku sku = productSkuMapper.selectById(skuId);
        if(Objects.isNull(sku) || sku.getStock() - orderDTO.getCount() < 0) {
            return Result.fail("商品库存不足");
        }
        //更新库存
        Long userId = UserHolder.getUser().getId();
        sku.setStock(sku.getStock() - orderDTO.getCount());
        productSkuMapper.updateById(sku);
        long orderId = orderIdGenerate.nextId("order:");
        boolean suc;
        boolean isLock = false;
        //设置订单锁
        RLock lock = redissonClient.getLock(RedisConstants.LOCK_ORDER_KEY + userId);
        try{
            isLock = lock.tryLock();
            if(!isLock) {
               throw new Exception("稍后再试");
            }
            //获取代理对象防止事务失效
            IProductOrderService proxy = (IProductOrderService) AopContext.currentProxy();
            suc = proxy.buildOrder(orderId, orderDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(isLock && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        if(!suc) {
            return Result.fail("创建订单失败");
        }

        //事务提交后更新redis
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                stringRedisTemplate.opsForValue().increment(RedisConstants.PRODUCT_SALE_KEY + sku.getProductId(), orderDTO.getCount());
            }
        });

        return Result.ok(orderId);
    }

    @Override
    @Transactional
    public boolean buildOrder(long orderId, OrderDTO orderDTO) {
        Long userId = UserHolder.getUser().getId();
        BaseOrder baseOrder = new BaseOrder();
        baseOrder.setId(orderId);
        baseOrder.setType(SystemConstants.ORDER_TYPE_PRODUCT);
        Address address = addressMapper.selectOne(new LambdaQueryWrapper<Address>().eq(Address::getUserId, userId).eq(Address::getIsTop, SystemConstants.ADDRESS_IS_TOP));
        AddressDTO dto = BeanUtil.copyProperties(address, AddressDTO.class);
        baseOrder.setAddress(JSONUtil.toJsonStr(dto));
        baseOrderMapper.insert(baseOrder);
        ProductOrder productOrder = new ProductOrder();
        productOrder.setId(orderId);
        productOrder.setProductSkuId(orderDTO.getSkuId());
        productOrder.setCount(orderDTO.getCount());
        productOrderMapper.insert(productOrder);
        return true;
    }


}
