package com.luckybox.pojo.dto;

import lombok.Data;

@Data
public class LuckyBoxDTO {
    private String name;                   // 盲盒名称
    private String details;                // 盲盒详情
    private String tips;                   // 购买提示
    private Long price;                    // 价格（单位：分）
    private String cover;                  // 封面
    private Long categoryId;               // 类别ID
    private String categoryName;               // 类别ID
    private int boxType;                // 盲盒类型：0-商品盲盒, 1-抽卡盲盒
    private Boolean status;                // 状态：0-启用, 1-禁用
    private Long totalStock;                //总库存
    private Long soldCount;
}
