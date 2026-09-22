package com.luckybox.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryPageVO {
    private Long id;                       // 类别ID
    private LocalDateTime createdTime;     // 创建时间
    private LocalDateTime updateTime;      // 更新时间
    private String name;                   // 类别名称
    private Long parentId;                 // 父类别ID
    private String parentName;
    private String icon;                   // 类别图标
    private String description;            // 描述
    private Integer sortOrder;             // 排序号

    private List<ProductCategoryPageVO> children ;
}
