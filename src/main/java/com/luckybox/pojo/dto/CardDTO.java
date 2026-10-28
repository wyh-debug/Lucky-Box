package com.luckybox.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardDTO {
    private String name;                   // 卡片名称
    private String description;            // 卡片描述
    private String icon;                   // 卡片图标
    private Long cardTypeId;               // 卡片类型ID


    private String cardTypeName;             //卡片类型名称
    private String cardTypeCode;            //卡片类型编码


    private BigDecimal probability;        // 基础抽中概率（0~1）
    private Boolean isLimited;             // 是否为限定卡片
    private Boolean status;                // 卡片状态：0-启用, 1-禁用
    private Integer stock;                 // 总发行量（NULL表示无限）
}
