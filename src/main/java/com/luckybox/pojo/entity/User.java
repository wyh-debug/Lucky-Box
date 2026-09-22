package com.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@Accessors(chain = true)
@TableName("user")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;                       // 用户ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    private String nickname;               // 昵称
    private String avatar;                 // 头像
    private String gender;                 // 性别
    private String phone;                  // 手机号
    private String password;               // 密码

    @Default
    private Integer status = 0;                 // 用户状态

    private String role;                   // 角色：USER-普通用户, ADMIN-管理员
}