package com.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("user_we_chat")
public class UserWeChat implements Serializable {
    private static final long serialVersionUID = 3L;

    @TableId(type = IdType.AUTO)
    private Long id;                       // 主键

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    private Long userId;                   // 用户ID
    private String openId;                 // 微信open_id
}