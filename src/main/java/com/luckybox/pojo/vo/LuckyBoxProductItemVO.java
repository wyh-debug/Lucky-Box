package com.luckybox.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LuckyBoxProductItemVO {

    private Long luckyBoxId;             // 盲盒ID
    //盲盒名称
    private String luckyBoxName;

    private Long productSkuId;             // 商品SKU ID
   //商品名称
    private String productName;
    private BigDecimal probability;        // 抽中概率（0~1）
    private Integer sortOrder;
}
