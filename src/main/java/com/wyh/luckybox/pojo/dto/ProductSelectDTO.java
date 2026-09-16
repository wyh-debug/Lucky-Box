package com.wyh.luckybox.pojo.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ProductSelectDTO {
    // 分页参数
    private Integer page = 1;   // 默认第一页
    private Integer size = 10;  // 默认每页10条

    private String name;
    private Long categoryId;
    private String categoryName;
    private String brand;
    private String tags;
    private Map<String, Object> specifications;         // 规格

    private Map<String, Object> attributes;             // 属性

    private String qualityType;            // 品质类型
    private Integer sortType;           //排序类型

}
