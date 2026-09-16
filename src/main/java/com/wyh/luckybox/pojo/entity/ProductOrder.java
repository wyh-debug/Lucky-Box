package com.wyh.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("product_order")
public class ProductOrder implements Serializable {
    private static final long serialVersionUID = 19L;

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

    private Long baseOrderId;           // 商品订单ID
    private Long productSkuId;             // 商品SKU ID
    private Integer count;                 // 数量
}