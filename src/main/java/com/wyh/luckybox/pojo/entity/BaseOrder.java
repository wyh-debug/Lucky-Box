package com.wyh.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("base_order")
public class BaseOrder implements Serializable {
    private static final long serialVersionUID = 16L;

    @TableId(type = IdType.AUTO)
    private Long id;                       // 订单ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;                // 创建人ID（用户ID）

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;                 // 更新人ID

    private Long paymentId;                // 支付ID
    private String type;                   // 订单类型：PRODUCT-商品, MYSTERY_BOX-盲盒
    private String status;                 // 订单状态
    private String address;                // 地址快照
    private String remark;                 // 备注
    private String trackingNumber;         // 物流单号
    private Long couponUserId;             // 用户优惠券ID
}