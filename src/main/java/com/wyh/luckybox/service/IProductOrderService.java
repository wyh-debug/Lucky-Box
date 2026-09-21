package com.wyh.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wyh.luckybox.pojo.dto.OrderDTO;
import com.wyh.luckybox.pojo.dto.Result;
import com.wyh.luckybox.pojo.entity.ProductOrder;

public interface IProductOrderService extends IService<ProductOrder> {
    Result addProductOrder(OrderDTO orderDTO);

    boolean buildOrder(long orderId, OrderDTO orderDTO);
}
