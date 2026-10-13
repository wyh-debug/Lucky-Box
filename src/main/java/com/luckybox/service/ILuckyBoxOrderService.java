package com.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.dto.OrderDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.LuckyBoxOrder;
import org.springframework.transaction.annotation.Transactional;

public interface ILuckyBoxOrderService extends IService<LuckyBoxOrder> {
    Result addBoxOrder(OrderDTO orderDTO);

}
