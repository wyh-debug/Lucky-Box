package com.luckybox.pojo.vo;

import com.luckybox.pojo.entity.ProductSku;
import com.luckybox.pojo.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductSkuVO {
    private Product product;
    private ProductSku productSku;
}
