package com.luckybox.pojo.dto;

import lombok.Data;

@Data
public class LuckyBoxSelectDTO extends PageQuery{
    private String name;                   // 盲盒名称
    private String details;                // 盲盒详情
    private Long price;                    // 价格（单位：分）
    private Long categoryId;               // 类别ID
    private int boxType;                // 盲盒类型：0-商品盲盒, 1-抽卡盲盒
    private Boolean status;             //状态
}
