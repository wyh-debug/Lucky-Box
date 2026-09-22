package com.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("user_coupon")
public class UserCoupon implements Serializable {
    private static final long serialVersionUID = 25L;

    @TableId(type = IdType.AUTO)
    private Long id;                       // 主键

    private Long userId;                   // 用户ID
    private Long couponId;                 // 优惠券ID
    private Integer status;                // 状态：1-未使用, 2-已使用, 3-已过期, 4-已退款

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime acquiredTime;    // 领取时间（列名 acquired_time）

    private LocalDateTime usedTime;        // 使用时间
    private LocalDateTime expireTime;      // 过期时间

    private Long baseOrderId;              // 使用的订单ID（关联base_order）
    private String sourceType;             // 来源：PURCHASE-付费购买, SECKILL-秒杀, ADMIN-后台赠送
    private Long sourceOrderId;            // 来源订单ID（购买时关联seckill_order.id）

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;      // 创建时间（列名 create_time）

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间（列名 update_time）
}