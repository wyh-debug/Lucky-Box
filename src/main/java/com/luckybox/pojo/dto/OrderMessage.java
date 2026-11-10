package com.luckybox.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage implements Serializable {
    private Long orderId;     // 预生成的订单ID
    private Long userId;
    private Long skuId;
    private Long addressId;
    private Integer count;
    private Long productId;   // 用于后续更新销量
    private String remark;//备注
}