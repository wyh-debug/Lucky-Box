package com.luckybox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.mapper.LuckyBoxProductMapper;
import com.luckybox.pojo.entity.LuckyBoxProduct;
import com.luckybox.service.ILuckyBoxProductService;
import org.springframework.stereotype.Service;

@Service
public class LuckyBoxProductServiceImpl extends ServiceImpl<LuckyBoxProductMapper, LuckyBoxProduct> implements ILuckyBoxProductService {

}
