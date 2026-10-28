package com.luckybox.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardSelectDTO extends PageQuery{
    private String name;                   // 卡片名称
    private Boolean isLimited;             // 是否为限定卡片
    private Boolean status;                // 卡片状态：0-启用, 1-禁用

}
