package com.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("seckill_order")
public class SeckillOrder implements Serializable {
    private static final long serialVersionUID = 26L;

    @TableId(type = IdType.AUTO)
    private Long id;                       // 订单ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;                // 下单用户ID

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;                 // 更新人ID

    private String orderNo;                // 订单编号（唯一）
    private Long couponId;                 // 购买的秒杀券ID
    private Integer quantity;              // 购买数量
    private Long unitPrice;                // 单价（单位：分）
    private Long totalPrice;               // 总价（单位：分）
    private Integer payType;               // 支付方式：1-微信, 2-支付宝, 3-余额
    private Integer status;                // 订单状态：1-待支付, 2-已支付, 3-已取消, 4-已退款
    private LocalDateTime payTime;         // 支付时间
    private LocalDateTime cancelTime;      // 取消时间
    private LocalDateTime refundTime;      // 退款时间
    private String tradeNo;                // 第三方支付交易号
    private String refundNo;               // 第三方退款单号
}