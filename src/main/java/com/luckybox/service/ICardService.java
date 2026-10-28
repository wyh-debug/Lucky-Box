package com.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.dto.CardSelectDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.Card;
import org.springframework.stereotype.Service;


public interface ICardService extends IService<Card> {
    Result createCard(Card card);

    Result delete(Long id);

    Result updateCard(Card card);

    Result pageQuery(CardSelectDTO cardSelectDTO);

    Result getCard(Long id);
}
