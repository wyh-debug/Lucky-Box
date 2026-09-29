package com.luckybox.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LuckyBoxProductsVO {
    private Long luckyBoxId;
    //盲盒名称
    private String name;
    private List<LuckyBoxProductItemVO> list;
}
