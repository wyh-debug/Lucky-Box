package com.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.dto.ProductSelectDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.Product;

public interface IProductService extends IService<Product> {
    Result addProduct(Product product);

    Result updateProduct(Product product);

    Result getProduct(Long id) throws InterruptedException ;

    Result getProductList(ProductSelectDTO productSelectDTO);

    Result deleteProduct(Long id);

    Result sortProduct(Integer sort, Integer pageNum, Integer pageSize);
}
