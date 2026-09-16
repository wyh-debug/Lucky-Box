package com.wyh.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wyh.luckybox.pojo.dto.Result;
import com.wyh.luckybox.pojo.entity.ProductSku;

public interface IProductSkuService extends IService<ProductSku> {

    Result addProductSKu(ProductSku productSku);

    Result updateProductSku(ProductSku productSku);

    Result deleteProductSku(Long id);

    Result selectByProductId(Long productId) throws InterruptedException;

    Result selectByProductSkuId(Long productSkuId) throws InterruptedException;
}
