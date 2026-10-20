package com.luckybox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.mapper.CardMapper;
import com.luckybox.pojo.entity.Card;
import com.luckybox.service.ICardService;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements ICardService {
}
