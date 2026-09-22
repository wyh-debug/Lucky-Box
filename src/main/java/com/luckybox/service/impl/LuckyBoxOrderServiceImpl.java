package com.luckybox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.mapper.LuckyBoxOrderMapper;
import com.luckybox.pojo.entity.LuckyBoxOrder;
import com.luckybox.service.ILuckyBoxOrderService;
import org.springframework.stereotype.Service;

@Service
public class LuckyBoxOrderServiceImpl extends ServiceImpl<LuckyBoxOrderMapper, LuckyBoxOrder> implements ILuckyBoxOrderService {
}
