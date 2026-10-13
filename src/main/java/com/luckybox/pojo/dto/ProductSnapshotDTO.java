package com.luckybox.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.util.Map;

@Data
public class ProductSnapshotDTO {
    private Long productId;                // 商品ID
    private Map<String, Object> specification;
    private String cover;                  // 规格封面
    private Long price;                    // 价格（单位：分）
    private Long stock;                 // 库存
    private String description;            // 描述
    //product
    private String name;                   // 名称
    private String brand;                  // 品牌
    private Map<String, Object> attributes;             // 属性
    private String qualityType;            // 品质类型
}
