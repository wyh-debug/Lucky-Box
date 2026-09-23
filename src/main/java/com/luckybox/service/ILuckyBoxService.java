package com.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.dto.LuckyBoxSelectDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.LuckyBox;

public interface ILuckyBoxService extends IService<LuckyBox> {
    Result addLuckyBox(LuckyBox luckyBox);

    Result deleteLuckyBox(Long id);

    Result updateLuckyBox(LuckyBox luckyBox);

    Result getLuckyBoxList(LuckyBoxSelectDTO luckyBoxSelectDTO);

    Result getLuckyBox(Long id);
}
