package com.wyh.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("payment")
public class Payment implements Serializable {
    private static final long serialVersionUID = 17L;

    @TableId(type = IdType.AUTO)
    private Long id;                       // 支付ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;                // 创建人ID

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;                 // 更新人ID

    private String payType;                // 支付方式
    private LocalDateTime payTime;         // 支付时间
    private Long payAmount;                // 实付金额（单位：分）
    private Long couponAmount;             // 优惠券优惠金额（单位：分）
    private Long productAmount;            // 商品总价（单位：分）
    private String tradeNo;                // 第三方交易号
}