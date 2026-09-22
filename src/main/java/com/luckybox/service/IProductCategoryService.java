package com.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.dto.ProductCategorySelectDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.ProductCategory;

public interface IProductCategoryService extends IService<ProductCategory> {
    Result pageProductCategory(ProductCategorySelectDTO productCategorySelectDTO);
    Result saveProductCategory(ProductCategory productCategory);

    Result pageList();

    Result deleteProductCategory(Long id);
}
