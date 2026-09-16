package com.wyh.luckybox.pojo.vo;

import com.wyh.luckybox.pojo.entity.Product;
import com.wyh.luckybox.pojo.entity.ProductSku;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductSkuVO {
    private Product product;
    private ProductSku productSku;
}
