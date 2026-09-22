package com.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Accessors(chain = true)
@TableName("product_sku")
public class ProductSku implements Serializable {
    private static final long serialVersionUID = 6L;

    @TableId(type = IdType.AUTO)
    private Long id;                       // SKU ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;                // 创建人ID

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;                 // 更新人ID

    private Long productId;                // 商品ID
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> specification;
    private String cover;                  // 规格封面
    private Long price;                    // 价格（单位：分）
    private Integer stock;                 // 库存
    private String description;            // 描述
}