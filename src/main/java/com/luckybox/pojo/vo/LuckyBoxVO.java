package com.luckybox.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LuckyBoxVO {
    private Long id;                       // 盲盒ID

    private LocalDateTime createdTime;     // 创建时间

    private LocalDateTime updateTime;      // 更新时间

    private Long creatorId;                // 创建人ID

    private Long updateId;                 // 更新人ID

    private String name;                   // 盲盒名称
    private String details;                // 盲盒详情
    private String tips;                   // 购买提示
    private Long price;                    // 价格（单位：分）
    private String cover;                  // 封面
    private Long categoryId;               // 类别ID
    //新增
    private String categoryName;
    private int boxType;                // 盲盒类型：0-商品盲盒, 1-抽卡盲盒
    private Boolean status;
}
