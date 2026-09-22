package com.luckybox.pojo.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ProductSkuDTO {
    @Data
    @Accessors(chain = true)
    @TableName("product_sku")
    public class ProductSku implements Serializable {
        private static final long serialVersionUID = 6L;
        private Long id;                       // SKU ID
        private LocalDateTime createdTime;     // 创建时间
        private LocalDateTime updateTime;      // 更新时间
        private Long creatorId;                // 创建人ID
        private Long updateId;                 // 更新人ID
        private Long productId;                // 商品ID
        private String ProductName;         //商品名称 原数据库没有关联
        private Map<String, Object> specification;
        private String cover;                  // 规格封面
        private Long price;                    // 价格（单位：分）
        private Integer stock;                 // 库存
        private String description;            // 描述
    }
}
