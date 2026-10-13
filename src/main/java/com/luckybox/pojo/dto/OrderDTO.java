package com.luckybox.pojo.dto;

import lombok.Data;

/**
 * 下单请求
 */
@Data
public class OrderDTO {
    //商品sku id 或者盲盒id
    private Long id;

    //数量
    private int count = 1;
    //收货地址id
    private Long addressId;
}
