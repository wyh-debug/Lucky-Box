package com.wyh.luckybox.pojo.dto;

import lombok.Data;

@Data
public class PageQuery {
    //当前页码
    private Integer page = 1;
    //每页条数
    private Integer size = 10;

    /** 排序字段（可选，用于动态排序） */
    private String orderField;

    /** 排序方向：asc / desc（可选） */
    private String orderDirection;

    private Integer sortType;           //排序类型
}
