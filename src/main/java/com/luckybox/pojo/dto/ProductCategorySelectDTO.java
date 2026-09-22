package com.luckybox.pojo.dto;

import lombok.Data;

@Data
public class ProductCategorySelectDTO extends PageQuery{

    private String name;
    private Long parentId;
    //父类别名称
    private String parentName;


}
