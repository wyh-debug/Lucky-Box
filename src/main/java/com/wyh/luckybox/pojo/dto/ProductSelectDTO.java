package com.wyh.luckybox.pojo.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ProductSelectDTO extends PageQuery{

    private String name;
    private Long categoryId;
    private String categoryName;
    private String brand;
    private String tags;
    private Map<String, Object> specifications;         // 规格

    private Map<String, Object> attributes;             // 属性

    private String qualityType;            // 品质类型


}
