package com.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.dto.LuckyBoxCategorySelectDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.LuckyBoxCategory;

public interface ILuckyBoxCategoryService extends IService<LuckyBoxCategory> {
    Result deleteBoxCategory(Long id);

    Result getBoxCategoryList(LuckyBoxCategorySelectDTO luckyBoxCategorySelectDTO);
}
