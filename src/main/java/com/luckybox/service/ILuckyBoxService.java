package com.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.dto.LuckyBoxSelectDTO;
import com.luckybox.pojo.dto.OrderDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.LuckyBox;
import org.springframework.transaction.annotation.Transactional;

public interface ILuckyBoxService extends IService<LuckyBox> {
    Result addLuckyBox(LuckyBox luckyBox);

    Result deleteLuckyBox(Long id);

    Result updateLuckyBox(LuckyBox luckyBox);

    Result getLuckyBoxList(LuckyBoxSelectDTO luckyBoxSelectDTO);

    Result getLuckyBox(Long id);

}
