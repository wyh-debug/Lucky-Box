package com.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("card")
public class Card implements Serializable {
    private static final long serialVersionUID = 11L;

    @TableId(type = IdType.AUTO)
    private Long id;                       // 卡片ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;                // 创建人ID

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;                 // 更新人ID

    private String name;                   // 卡片名称
    private String description;            // 卡片描述
    private String icon;                   // 卡片图标
    private Long cardTypeId;               // 卡片类型ID
    private BigDecimal probability;        // 基础抽中概率（0~1）
    private Boolean isLimited;             // 是否为限定卡片
    private Boolean status;                // 卡片状态：0-启用, 1-禁用
    private Integer stock;                 // 总发行量（NULL表示无限）
}