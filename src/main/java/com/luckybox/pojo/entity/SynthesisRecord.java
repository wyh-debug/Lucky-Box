package com.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("synthesis_record")
public class SynthesisRecord implements Serializable {
    private static final long serialVersionUID = 15L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;                // 创建人ID

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;                 // 更新人ID

    private Long userId;                   // 用户ID
    private Long ruleId;                   // 合成规则ID
    private String consumedCardIds;        // 消耗的卡片ID列表（JSON）
    private Long resultProductSkuId;          // 获得的商品ID
    private String status;                 // 合成状态
}