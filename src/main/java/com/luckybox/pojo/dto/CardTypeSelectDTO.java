package com.luckybox.pojo.dto;

import lombok.Data;

@Data
public class CardTypeSelectDTO extends PageQuery{
    private String code;
    private String name;
}
