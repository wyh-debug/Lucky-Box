package com.wyh.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("coupon_seckill")
public class CouponSeckill implements Serializable {
    private static final long serialVersionUID = 24L;

    @TableId(type = IdType.INPUT)   // 主键为 coupon_id，非自增
    private Long couponId;                  // 优惠券ID（主键，一对一关联）

    private Integer stock;                  // 秒杀剩余库存

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;       // 创建时间（注意列名是 create_time）

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;       // 更新时间（注意列名是 update_time）

    private LocalDateTime beginTime;        // 秒杀开始时间
    private LocalDateTime endTime;          // 秒杀结束时间
}