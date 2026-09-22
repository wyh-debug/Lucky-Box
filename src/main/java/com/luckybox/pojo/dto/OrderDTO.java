package com.luckybox.pojo.dto;

import lombok.Data;

/**
 * 下单请求
 */
@Data
public class OrderDTO {
    //商品sku id
    private Long skuId;

    //数量
    private int count = 1;
}
