package com.luckybox.pojo.vo;

import com.luckybox.pojo.entity.Product;
import com.luckybox.pojo.entity.ProductSku;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ProductDetailVO {

    private Product product;
    private List<ProductSku> skuList;
    private Long defaultSkuId;




}
