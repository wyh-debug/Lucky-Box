package com.luckybox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.mapper.LuckyBoxCardMapper;
import com.luckybox.pojo.entity.LuckyBoxCard;
import com.luckybox.service.ILuckyBoxCardService;
import org.springframework.stereotype.Service;

@Service
public class LuckyBoxCardServiceImpl extends ServiceImpl<LuckyBoxCardMapper, LuckyBoxCard> implements ILuckyBoxCardService {
}
