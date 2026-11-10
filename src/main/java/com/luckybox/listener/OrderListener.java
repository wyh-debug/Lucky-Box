package com.luckybox.listener;

import cn.hutool.json.JSONUtil;
import com.luckybox.constant.MqConstants;
import com.luckybox.constant.RedisConstants;
import com.luckybox.mapper.AddressMapper;
import com.luckybox.mapper.BaseOrderMapper;
import com.luckybox.mapper.ProductOrderMapper;
import com.luckybox.mapper.ProductSkuMapper;
import com.luckybox.pojo.dto.OrderMessage;
import com.luckybox.pojo.entity.BaseOrder;
import com.luckybox.pojo.entity.ProductOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderListener {
    private final ProductSkuMapper productSkuMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final AddressMapper addressMapper;
    private final BaseOrderMapper baseOrderMapper;
    private final ProductOrderMapper productOrderMapper;

    /**
     * 调用数据库，创建订单
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConstants.ORDER_INSERT_QUEUE, durable = "true"),
            exchange = @Exchange(value = MqConstants.ORDER_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = MqConstants.ORDER_INSERT_KEY
    ))
    @Transactional
    public void handleOrderCreate(OrderMessage msg) {
        //数据库，库存减
        boolean suc = productSkuMapper.deductStock(msg.getSkuId(), msg.getCount());
        //失败redis回调
        if(!suc) {
            stringRedisTemplate.opsForValue().increment(RedisConstants.LOCK_PRODUCT_STOCK + msg.getSkuId());
        }
        //创建订单
        BaseOrder baseOrder = new BaseOrder();
        baseOrder.setId(msg.getOrderId());
        baseOrder.setAddress(JSONUtil.toJsonStr(addressMapper.selectById(msg.getAddressId())));
        baseOrder.setRemark(msg.getRemark() == null ? "" : msg.getRemark());
        baseOrderMapper.insert(baseOrder);
        //创建子订单
        ProductOrder productOrder = new ProductOrder();
        productOrder.setId(msg.getOrderId());
        productOrder.setProductSkuId(msg.getSkuId());
        productOrder.setCount(msg.getCount());
        productOrderMapper.insert(productOrder);
    }
}
