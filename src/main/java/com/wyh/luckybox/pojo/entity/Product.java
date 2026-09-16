package com.wyh.luckybox.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Accessors(chain = true)
@TableName("product")
public class Product implements Serializable {
    private static final long serialVersionUID = 5L;

    @TableId(type = IdType.AUTO)
    private Long id;                       // 商品ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;                // 创建人ID

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;                 // 更新人ID

    private String name;                   // 名称
    private Long price;                    // 价格（单位：分）
    private String cover;                  // 封面
    private String brand;                  // 品牌
    private Long categoryId;               // 类别ID
    private String description;            // 描述
    private String tags;                   // 标签

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> specifications;         // 规格

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> attributes;             // 属性

    private String qualityType;            // 品质类型

    private Long sale;              //销量
}