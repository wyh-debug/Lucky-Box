package com.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("lucky_box_category")
public class LuckyBoxCategory implements Serializable {
    private static final long serialVersionUID = 7L;

    @TableId(type = IdType.AUTO)
    private Long id;                       // 类别ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;                // 创建人ID

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;                 // 更新人ID

    private String name;                   // 类别名称
    private String icon;                   // 类别图标
    private String description;            // 描述
    private Integer sortOrder;             // 排序号
}