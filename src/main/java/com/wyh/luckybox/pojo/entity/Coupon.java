package com.wyh.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("coupon")
public class Coupon implements Serializable {
    private static final long serialVersionUID = 23L;

    @TableId(type = IdType.AUTO)
    private Long id;                       // 优惠券ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;                // 创建人ID

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;                 // 更新人ID

    private String name;                   // 优惠券名称
    private String subName;                // 副标题
    private String rules;                  // 使用规则
    private Integer discountType;          // 优惠类型：1-满减, 2-折扣
    private Long discountValue;            // 优惠数值（满减填金额(分)，折扣填系数如80=8折）
    private Long thresholdPrice;           // 使用门槛（单位：分），0表示无门槛
    private Long salePrice;                // 购买售价（单位：分），0表示免费
    private Integer scopeType;             // 适用范围：0-全场, 1-仅盲盒, 2-仅商品
    private Integer type;                  // 券类型：0-普通券, 1-秒杀券
    private Integer status;                // 状态：1-上架, 2-下架, 3-过期
    private Integer totalQuantity;         // 发行总量（NULL表示无限）
    private Integer usedQuantity;          // 已使用数量
}