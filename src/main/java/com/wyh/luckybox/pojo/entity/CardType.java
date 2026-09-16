package com.wyh.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("card_type")
public class CardType implements Serializable {
    private static final long serialVersionUID = 10L;

    @TableId(type = IdType.AUTO)
    private Long id;                       // 卡片类型ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;                // 创建人ID

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;                 // 更新人ID

    private String code;                   // 类型编码（唯一），如 WESTERN_JOURNEY
    private String name;                   // 类型名称，如 西游卡
    private String description;            // 类型描述
}