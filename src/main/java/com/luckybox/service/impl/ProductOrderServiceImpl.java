package com.luckybox.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.constant.MqConstants;
import com.luckybox.constant.RedisConstants;
import com.luckybox.mapper.BaseOrderMapper;
import com.luckybox.mapper.ProductOrderMapper;
import com.luckybox.mapper.ProductSkuMapper;
import com.luckybox.pojo.dto.OrderDTO;
import com.luckybox.pojo.dto.OrderMessage;
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
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private RabbitTemplate rabbitTemplate;
    @Override
    @Transactional
    public Result addProductOrder(OrderDTO orderDTO) {

        Long skuId = orderDTO.getId();
        ProductSku sku = productSkuMapper.selectById(skuId);
        if(sku == null) {
            Result.fail("商品不存在");
        }
        //更新库存
        Long remain = stringRedisTemplate.opsForValue().decrement(RedisConstants.LOCK_PRODUCT_STOCK + orderDTO.getId(), orderDTO.getCount());
        if(remain == null || remain < 0) {
            stringRedisTemplate.opsForValue().increment(RedisConstants.LOCK_PRODUCT_STOCK + orderDTO.getId(), orderDTO.getCount());
            return Result.fail("库存不足");
        }
        long orderId = orderIdGenerate.nextId("order:");
        assert sku != null;
        OrderMessage orderMessage = new OrderMessage(orderId, UserHolder.getUser().getId(), orderDTO.getId(),
                orderDTO.getAddressId(), orderDTO.getCount(), sku.getProductId(), orderDTO.getRemark()
        );
        //发送消息
        try {
            rabbitTemplate.convertAndSend(MqConstants.ORDER_TOPIC_EXCHANGE, MqConstants.ORDER_INSERT_KEY, orderMessage);
        } catch (AmqpException e) {
            stringRedisTemplate.opsForValue().increment(RedisConstants.LOCK_PRODUCT_STOCK + orderDTO.getId(), orderDTO.getCount());
            return Result.fail("系统繁忙");
        }
        return Result.ok(orderId);
    }
//
//    @Override
//    @Transactional
//    public boolean buildOrder(long orderId, OrderDTO orderDTO) {
//        Long userId = UserHolder.getUser().getId();
//        BaseOrder baseOrder = new BaseOrder();
//        baseOrder.setId(orderId);
//        baseOrder.setType(SystemConstants.ORDER_TYPE_PRODUCT);
//        Address address = addressMapper.selectById(orderDTO.getAddressId());
//        AddressDTO dto = BeanUtil.copyProperties(address, AddressDTO.class);
//        baseOrder.setAddress(JSONUtil.toJsonStr(dto));
//        baseOrderMapper.insert(baseOrder);
//        ProductOrder productOrder = new ProductOrder();
//        productOrder.setId(orderId);
//        productOrder.setProductSkuId(orderDTO.getId());
//        productOrder.setCount(orderDTO.getCount());
//        productOrderMapper.insert(productOrder);
//        return true;
//    }

    @Override
    public Long addOrder(Long id, Long skuId, Long addressId, int count) {
        long orderId = orderIdGenerate.nextId("order:");
        BaseOrder baseOrder = new BaseOrder();
        baseOrder.setId(orderId);
        baseOrder.setType(SystemConstants.ORDER_TYPE_PRODUCT);
        Address address = addressMapper.selectById(addressId);
        AddressDTO dto = BeanUtil.copyProperties(address, AddressDTO.class);
        baseOrder.setAddress(JSONUtil.toJsonStr(dto));
        baseOrderMapper.insert(baseOrder);
        ProductOrder productOrder = new ProductOrder();
        productOrder.setId(orderId);
        productOrder.setProductSkuId(id);
        productOrder.setCount(count);
        productOrder.setProductSkuId(skuId);
        productOrderMapper.insert(productOrder);
        return orderId;
    }


}
