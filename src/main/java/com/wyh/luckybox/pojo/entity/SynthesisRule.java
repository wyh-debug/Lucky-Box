package com.wyh.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("synthesis_rule")
public class SynthesisRule implements Serializable {
    private static final long serialVersionUID = 14L;

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

    private String name;                   // 合成配方名称
    private String description;            // 配方描述
    private String conditionRule;          // 合成条件规则（JSON）
    private Long resultProductSkuId;          // 合成获得的商品ID
    private String resultProductSnapshot;  // 商品快照（JSON）
    private Boolean enabled;               // 是否启用
    private Integer totalQuota;            // 总合成配额（NULL表示无限）
    private Integer usedQuota;             // 已合成次数
}