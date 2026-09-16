package com.wyh.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("address")
public class Address implements Serializable {
    private static final long serialVersionUID = 2L;

    @TableId(type = IdType.AUTO)
    private Long id;                       // 地址ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;                // 创建人ID

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;                 // 更新人ID

    private Long userId;                   // 所属用户ID
    private Double latitude;               // 纬度
    private Double longitude;              // 经度
    private String province;               // 省
    private String city;                   // 市
    private String district;               // 区
    private String details;                // 详细地址
    private String houseNumber;            // 门牌号
    private String phoneNumber;            // 手机号
    private String realName;               // 真实姓名

    @TableField("is_top")
    private Boolean isTop;                 // 是否置顶
}