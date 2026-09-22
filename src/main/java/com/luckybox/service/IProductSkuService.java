package com.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.entity.ProductSku;
import com.luckybox.pojo.dto.Result;

public interface IProductSkuService extends IService<ProductSku> {

    Result addProductSKu(ProductSku productSku);

    Result updateProductSku(ProductSku productSku);

    Result deleteProductSku(Long id);

    Result selectByProductId(Long productId) throws InterruptedException;

    Result selectByProductSkuId(Long productSkuId) throws InterruptedException;
}
