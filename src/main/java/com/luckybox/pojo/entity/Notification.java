package com.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("notification")
public class Notification implements Serializable {
    private static final long serialVersionUID = 11L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;

    private Long userId;      // 接收用户ID
    private String title;     // 标题
    private String content;   // 内容
    private String type;      // 类型
    private Long bizId;       // 关联业务ID
    private Boolean isRead;   // 是否已读
}