package com.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.LuckyBoxCard;

import java.util.List;

public interface ILuckyBoxCardService extends IService<LuckyBoxCard> {
    Result create(LuckyBoxCard luckyBoxCard);

    Result createBatch(List<LuckyBoxCard> list);
}
