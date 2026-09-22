package com.luckybox.pojo.dto;

import lombok.Data;

@Data
public class LuckyBoxCategorySelectDTO extends PageQuery{
    private Long id;
    private String name;
}
