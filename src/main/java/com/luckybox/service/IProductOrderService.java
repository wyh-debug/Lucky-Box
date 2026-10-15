package com.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.dto.OrderDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.ProductOrder;

public interface IProductOrderService extends IService<ProductOrder> {
    Result addProductOrder(OrderDTO orderDTO);

    boolean buildOrder(long orderId, OrderDTO orderDTO);

    /**
     * 魔盒直接发货，id是抽奖记录id
     * @param id
     * @return 订单id
     */
    Long addOrder(Long id, Long skuId, Long addressId, int count);
}
