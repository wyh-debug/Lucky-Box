package com.wyh.luckybox.pojo.dto;

import lombok.Data;

@Data
public class ProductCategorySelectDTO{
    // 分页参数
    private Integer page = 1;   // 默认第一页
    private Integer size = 10;  // 默认每页10条

    private String name;
    private Long parentId;
    //父类别名称
    private String parentName;


}
