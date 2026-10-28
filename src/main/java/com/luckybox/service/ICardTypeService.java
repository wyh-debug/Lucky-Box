package com.luckybox.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.dto.CardTypeSelectDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.CardType;

public interface ICardTypeService extends IService<CardType> {
    Result pageQuery(CardTypeSelectDTO cardTypeSelectDTO);

    Result removeCardType(Long id);
}
