package com.wyh.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("lucky_box")
public class LuckyBox implements Serializable {
    private static final long serialVersionUID = 8L;

    @TableId(type = IdType.AUTO)
    private Long id;                       // 盲盒ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;                // 创建人ID

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;                 // 更新人ID

    private String name;                   // 盲盒名称
    private String details;                // 盲盒详情
    private String tips;                   // 购买提示
    private Long price;                    // 价格（单位：分）
    private String cover;                  // 封面
    private Long categoryId;               // 类别ID
    private String boxType;                // 盲盒类型：PRODUCT-商品盲盒, CARD-抽卡盲盒
    private Boolean status;                // 状态：0-启用, 1-禁用
}