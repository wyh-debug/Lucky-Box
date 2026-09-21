package com.wyh.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@TableName("product_order")
public class ProductOrder implements Serializable {
    private static final long serialVersionUID = 19L;

    @TableId(value = "base_order_id")
    private Long id;
    private Long productSkuId;             // 商品SKU ID
    private Integer count;                 // 数量
}