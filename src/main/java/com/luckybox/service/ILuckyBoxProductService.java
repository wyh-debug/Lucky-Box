package com.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.LuckyBoxProduct;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ILuckyBoxProductService extends IService<LuckyBoxProduct> {
    Result addLuckyBoxProduct(LuckyBoxProduct luckyBoxProduct);

    Result deleteLuckyBoxProduct(Long id);

    Result updateLuckyBoxProduct(LuckyBoxProduct luckyBoxProduct);

    Result getLuckyBoxProducts();

    Result batchAddLuckyBoxProduct(List<LuckyBoxProduct> luckyBoxProducts);

    Result deleteLuckyBoxProducts(List<Long> ids);
}
