package com.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("refund_record")
public class RefundRecord implements Serializable {
    private static final long serialVersionUID = 22L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;                // 创建人ID

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;                 // 更新人ID

    private Long orderId;                  // 关联的基础订单ID
    private String reason;                 // 退款理由
    private Long amount;                   // 退款金额（单位：分）
    private String status;                 // 退款状态
    private String refundApplicationDetails; // 退款申请详情
    private String refundNotifyDetails;    // 退款回调详情
    private String refundId;               // 第三方退款单号
}