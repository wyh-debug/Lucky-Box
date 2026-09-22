package com.luckybox.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ProductVO {
    private Long id;                       // 商品ID
    private LocalDateTime createdTime;     // 创建时间
    private String name;                   // 名称
    private Long price;                    // 价格（单位：分）
    private String cover;                  // 封面
    private String brand;                  // 品牌
    private Long categoryId;               // 类别ID
    private String categoryName;//新增的
    private String description;            // 描述
    private String tags;                   // 标签

    private Map<String, Object> specifications;         // 规格

    private Map<String, Object> attributes;             // 属性

    private String qualityType;
}
